package org.gateway.paygate.merchant.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.gateway.paygate.common.exception.RateLimitException;
import org.gateway.paygate.common.ratelimit.RateLimitResult;
import org.gateway.paygate.common.ratelimit.RateLimiter;
import org.gateway.paygate.merchant.cache.ApiKeyCache;
import org.gateway.paygate.merchant.cache.ApiKeyCacheEntry;
import org.gateway.paygate.merchant.entity.ApiKey;
import org.gateway.paygate.merchant.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.objenesis.instantiator.basic.NullInstantiator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private  final  MerchantContext merchantContext;
    private static  final String BASIC = "Basic ";
    private final ApiKeyRepository apiKeyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final RateLimiter rateLimiter;

    private final ApiKeyCache apiKeyCache;

    @Value("${app.rate-limit.max-requests-per-minute:60}")
    private Integer RequestPerMinute;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Incoming Request {}", request.getRequestURI());
    try {


            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith(BASIC)) {
                log.warn("No API key found in request headers for request: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            String [] credentials = decode(authorizationHeader);
            if(credentials == null) {
                throw new BadRequestException("Malformed API Key Header");

            }

            String keyId = credentials[0];
            String rawSecret = credentials[1];

             ApiKeyCacheEntry apiKeyCacheEntry = apiKeyCache.get(keyId).orElseGet(()->loadAndCacheApiKey(keyId));

            if(apiKeyCacheEntry==null || !apiKeyCacheEntry.enabled()|| !secretValidate(rawSecret,apiKeyCacheEntry)){
                throw new BadRequestException("Invalid or missing API Key");
            }

        RateLimitResult rateLimitResult = rateLimiter.check("apikey:"+keyId,RequestPerMinute,60);
        if(!rateLimitResult.isAllowed()){
            log.warn("API Key {} exceeded rate limit for request: {}", keyId, request.getRequestURI());
            throw new RateLimitException("To many Requests", rateLimitResult.retryAfterSeconds());
        }

        response.setHeader("X-RateLimit-Limit", String.valueOf(RequestPerMinute));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(rateLimitResult.remaining()));


        var auth = new UsernamePasswordAuthenticationToken(keyId, null,
                List.of(new SimpleGrantedAuthority("API_KEY_ROLE"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        merchantContext.setMerchantId(apiKeyCacheEntry.merchantId());
        merchantContext.setKeyId(apiKeyCacheEntry.keyId());
        filterChain.doFilter(request, response);
    }
    catch (Exception e){
        handlerExceptionResolver.resolveException(request, response, null,e);

    }

    }

    private ApiKeyCacheEntry loadAndCacheApiKey(String keyId) {
                        ApiKey apiKey = apiKeyRepository.findByKeyId(keyId).orElse(null);
                        if(apiKey == null) return null;
                        ApiKeyCacheEntry apiKeyCacheEntry = new ApiKeyCacheEntry(
                                apiKey.getKeyId(),
                                apiKey.getKeySecretHash(),
                                apiKey.getPreviousKeySecretHash(),
                                apiKey.getGracePeriodExpiresAt(),
                                apiKey.getMerchant().getId(),
                                apiKey.getEnvironment(),
                                apiKey.isEnabled()
                        );
                        apiKeyCache.put(keyId, apiKeyCacheEntry);
                        return apiKeyCacheEntry;
    }


    private boolean secretValidate(String rawSecret , ApiKeyCacheEntry apiKey){
        if(bCryptPasswordEncoder.matches(rawSecret, apiKey.keySecretHash())){
            return true;
        }
         return apiKey.isInGracePeriod() && apiKey.previousSecretHash()!= null && bCryptPasswordEncoder.matches(rawSecret, apiKey.previousSecretHash());
    }

    private String [] decode(String header){
        String encodedCredentials = header.substring(BASIC.length());
        String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials), StandardCharsets.UTF_8);

        int colon = decodedCredentials.indexOf(':');
        if (colon == -1) {
            throw new IllegalArgumentException("Invalid basic authentication token");
        }
        return new String[]{decodedCredentials.substring(0, colon), decodedCredentials.substring(colon + 1)};
    }
}

package org.gateway.paygate.common.Idempotency;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.exception.IdempotencyConflictException;
import org.gateway.paygate.merchant.security.MerchantContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component

public class IdempotencyFilter extends OncePerRequestFilter{

    private static final Set<String> GUARDED_METHODS = Set.of("POST","PUT","PATCH");
    private static final Duration IN_PROGRESS_TTL = Duration.ofSeconds(30);

    private final IdempotencyStore idempotencyStore;
    private final MerchantContext merchantContext;
    private final HandlerExceptionResolver handlerExceptionResolver;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!GUARDED_METHODS.contains(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String idemKey = request.getHeader("X-Idempotency-Key");
        if (idemKey == null || idemKey.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID merchantId = merchantContext.getMerchantId();
        String key = merchantId != null ? merchantId + ":" + idemKey : idemKey;

        boolean claimed = idempotencyStore.setIfAbsent(key, IN_PROGRESS_TTL);
        if (!claimed) {
            log.warn("Idempotency key {} already seen for merchant {}", idemKey, merchantId);
            Optional<String> existing = idempotencyStore.get(key);

            if (existing.isPresent() && !IdempotencyStore.IN_PROGRESS.equals(existing.get())) {
                replay(existing.get(), response);
            } else {
                handlerExceptionResolver.resolveException(request, response, null,
                        new IdempotencyConflictException("Request with this idempotency key is already in progress."));
            }
            return;
        }

        // First time: we own the key
        ContentCachingResponseWrapper wrapped = new ContentCachingResponseWrapper(response);
        boolean completed = false;
        try {
            filterChain.doFilter(request, wrapped);
            completed = true;
        } finally {
            try {
                int status = wrapped.getStatus();
                if (completed && status < 400) {
                    String body = new String(wrapped.getContentAsByteArray(), StandardCharsets.UTF_8);
                    idempotencyStore.store(key, status + "|" + body, Duration.ofHours(24));
                    log.debug("Stored response for idempotency key {} (merchant {})", idemKey, merchantId);
                } else {
                    idempotencyStore.delete(key);
                    log.debug("Released idempotency key {} (merchant {}), status={}", idemKey, merchantId, status);
                }
            } finally {
                wrapped.copyBodyToResponse(); // always flush the real response
            }
        }
    }

    private void replay(String stored, HttpServletResponse response) throws IOException {
        int sep = stored.indexOf('|');
        int status = Integer.parseInt(stored.substring(0, sep));
        String body = stored.substring(sep + 1);

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Idempotent-Replayed", "true");   // before the body is written

        if (!body.isEmpty()) {
            response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        }
    }
}


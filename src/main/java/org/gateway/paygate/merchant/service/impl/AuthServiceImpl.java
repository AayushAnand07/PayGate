package org.gateway.paygate.merchant.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.enums.MerchantStatus;
import org.gateway.paygate.common.enums.UserRole;
import org.gateway.paygate.common.exception.DuplicateResourceException;
import org.gateway.paygate.common.exception.ResourceNotFoundException;
import org.gateway.paygate.merchant.dto.request.LoginRequest;
import org.gateway.paygate.merchant.dto.request.MerchantSignupRequest;
import org.gateway.paygate.merchant.dto.response.LoginResponse;
import org.gateway.paygate.merchant.dto.response.MerchantResponse;
import org.gateway.paygate.merchant.entity.AppUsers;
import org.gateway.paygate.merchant.entity.Merchant;
import org.gateway.paygate.merchant.mapper.MerchantMapper;
import org.gateway.paygate.merchant.repository.AppUserRepository;
import org.gateway.paygate.merchant.repository.MerchantRepository;
import org.gateway.paygate.merchant.security.JwtUtil;
import org.gateway.paygate.merchant.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Override
    public MerchantResponse signup(MerchantSignupRequest request) {
        if(merchantRepository.existsByEmail(request.email())) {
            log.warn("Merchant with email {} already exists", request.email());
            throw new DuplicateResourceException("Merchant with email " + request.email() + " already exists", "MERCHANT_ALREADY_EXISTS");
        }
            Merchant merchant = merchantMapper.toEntityFromSignUpRequest(request);
            merchant.setMerchantStatus(MerchantStatus.PENDING_KYC);

            merchant = merchantRepository.save(merchant);

            AppUsers appUser = AppUsers.builder()
                    .email(request.email())
                    .merchant(merchant)
                    .passwordHash(passwordEncoder.encode(request.password()))
                    .role(UserRole.OWNER)
                    .build();
            appUserRepository.save(appUser);

            log.info("Merchant signed up successfully: {}", merchant.getId());

        return merchantMapper.toResponse(merchant);

    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        AppUsers appUser = appUserRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.email(), "USER_NOT_FOUND"));
        String token = jwtUtil.generateAccessToken(request.email(),appUser.getMerchant().getId(), appUser.getRole().toString());
        return new LoginResponse(token);
    }
}

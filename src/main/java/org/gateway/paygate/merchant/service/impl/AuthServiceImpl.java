package org.gateway.paygate.merchant.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.enums.MerchantStatus;
import org.gateway.paygate.common.enums.UserRole;
import org.gateway.paygate.common.exception.DuplicateResourceException;
import org.gateway.paygate.merchant.dto.request.MerchantSignupRequest;
import org.gateway.paygate.merchant.dto.response.MerchantResponse;
import org.gateway.paygate.merchant.entity.AppUsers;
import org.gateway.paygate.merchant.entity.Merchant;
import org.gateway.paygate.merchant.repository.AppUserRepository;
import org.gateway.paygate.merchant.repository.MerchantRepository;
import org.gateway.paygate.merchant.service.AuthService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;


    @Override
    public MerchantResponse signup(MerchantSignupRequest request) {
        if(merchantRepository.existsByEmail(request.email())) {
            log.warn("Merchant with email {} already exists", request.email());
            throw new DuplicateResourceException("Merchant with email " + request.email() + " already exists", "MERCHANT_ALREADY_EXISTS");
        }
            Merchant merchant = Merchant.builder()
                    .name(request.name())
                    .email(request.email())
                    .businessName(request.businessName())
                    .businessType(request.businessType())
                    .merchantStatus(MerchantStatus.PENDING_KYC)
                    .build();

            merchant = merchantRepository.save(merchant);

            AppUsers appUser = AppUsers.builder()
                    .email(request.email())
                    .merchant(merchant)
                    .passwordHash(request.password())
                    .role(UserRole.OWNER)
                    .build();
            appUserRepository.save(appUser);

            log.info("Merchant signed up successfully: {}", merchant.getId());

        return new MerchantResponse(
                merchant.getId(),
                merchant.getName(),
                merchant.getEmail(),
                merchant.getBusinessName(),
                merchant.getBusinessType(),
                merchant.getMerchantStatus()
        );
    }
}

package org.gateway.paygate.merchant.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.exception.ResourceNotFoundException;
import org.gateway.paygate.common.util.RandomizerUtil;
import org.gateway.paygate.merchant.dto.request.createApiKeyRequest;
import org.gateway.paygate.merchant.dto.response.ApiKeyResponse;
import org.gateway.paygate.merchant.dto.response.ApiKeyCreateResponse;
import org.gateway.paygate.merchant.entity.ApiKey;
import org.gateway.paygate.merchant.entity.Merchant;
import org.gateway.paygate.merchant.mapper.ApiKeyMapper;
import org.gateway.paygate.merchant.repository.ApiKeyRepository;
import org.gateway.paygate.merchant.repository.MerchantRepository;
import org.gateway.paygate.merchant.service.ApiKeyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {
    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;


    @Override
    @Transactional(readOnly = true)
    public ApiKeyCreateResponse create(UUID merchantId, createApiKeyRequest request) {
        Merchant merchant  = merchantRepository.findById(merchantId).orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        String keyId = "paygw_"+request.environment().name().toLowerCase()+"_"+RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);
        ApiKey apiKey  =ApiKey.builder()
                .keyId(keyId)
                .keySecretHash(rawSecret)
                .merchant(merchant)
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(
                apiKey.getId(),
                keyId,
                rawSecret,
                request.environment()
        );




    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return   apiKeyMapper.toApiKeyCreateResponseList(apiKeyRepository.findByMerchant_Id(merchantId));
    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey key = apiKeyRepository.findById(keyId).filter(apiKey -> apiKey.getMerchant().getId().equals(merchantId)).orElseThrow(() -> new ResourceNotFoundException("API Key", keyId));
        key.setEnabled(false);

    }

    @Override
    @Transactional
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId).
                filter(k -> k.getMerchant().getId().
                        equals(merchantId)).orElseThrow(() -> new ResourceNotFoundException("API Key", keyId));

        if(!apiKey.isEnabled()) throw  new RuntimeException("Cannot rotate a disabled API key");
        String newRawSecret = RandomizerUtil.randomBase64(40);
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(newRawSecret);
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));
//        apiKey = apiKeyRepository.save(apiKey);
        return new ApiKeyCreateResponse(
                apiKey.getId(),
                apiKey.getKeyId(),
                newRawSecret,
                apiKey.getEnvironment()
        );
    }
}

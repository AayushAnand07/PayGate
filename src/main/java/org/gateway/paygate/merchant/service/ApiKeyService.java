package org.gateway.paygate.merchant.service;

import jakarta.validation.Valid;
import org.gateway.paygate.merchant.dto.request.createApiKeyRequest;
import org.gateway.paygate.merchant.dto.response.ApiKeyResponse;
import org.gateway.paygate.merchant.dto.response.ApiKeyCreateResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {

    ApiKeyCreateResponse create(UUID merchantId, @Valid createApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}

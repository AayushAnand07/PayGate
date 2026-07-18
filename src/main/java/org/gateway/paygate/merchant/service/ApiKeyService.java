package org.gateway.paygate.merchant.service;

import jakarta.validation.Valid;
import org.gateway.paygate.merchant.dto.request.createApiKeyRequest;
import org.gateway.paygate.merchant.dto.response.ApiKeyResponse;
import org.gateway.paygate.merchant.dto.response.createApiKeyResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {

    createApiKeyResponse create(UUID merchantId, @Valid createApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    createApiKeyResponse rotate(UUID merchantId, UUID keyId);
}

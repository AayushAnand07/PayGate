package org.gateway.paygate.merchant.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gateway.paygate.merchant.dto.response.ApiKeyResponse;
import org.gateway.paygate.merchant.dto.request.createApiKeyRequest;
import org.gateway.paygate.merchant.dto.response.ApiKeyCreateResponse;
import org.gateway.paygate.merchant.security.MerchantContext;
import org.gateway.paygate.merchant.service.ApiKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;
    private final MerchantContext  merchantContext;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> create(@Valid @RequestBody createApiKeyRequest request) {
         return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.create(merchantContext.getMerchantId(), request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> get() {
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantContext.getMerchantId()));
    }

    @DeleteMapping("/keyId")
    public ResponseEntity<Void> delete(@PathVariable UUID keyId) {
        apiKeyService.revoke(merchantContext.getMerchantId(), keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotateKey(@PathVariable UUID keyId) {
        ApiKeyCreateResponse newKeyResponse = apiKeyService.rotate(merchantContext.getMerchantId(), keyId);
        return ResponseEntity.ok(newKeyResponse);
    }
}

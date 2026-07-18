package org.gateway.paygate.merchant.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gateway.paygate.merchant.dto.response.ApiKeyResponse;
import org.gateway.paygate.merchant.dto.request.createApiKeyRequest;
import org.gateway.paygate.merchant.dto.response.createApiKeyResponse;
import org.gateway.paygate.merchant.service.ApiKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<createApiKeyResponse> create(@PathVariable UUID merchantId, @Valid @RequestBody createApiKeyRequest request) {
         return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.create(merchantId, request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> get(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantId));
    }

    @DeleteMapping("/keyId")
    public ResponseEntity<Void> delete(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        apiKeyService.revoke(merchantId, keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<createApiKeyResponse> rotateKey(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        createApiKeyResponse newKeyResponse = apiKeyService.rotate(merchantId, keyId);
        return ResponseEntity.ok(newKeyResponse);
    }
}

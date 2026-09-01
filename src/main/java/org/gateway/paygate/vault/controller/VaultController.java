package org.gateway.paygate.vault.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gateway.paygate.merchant.security.MerchantContext;
import org.gateway.paygate.vault.dto.request.TokenizeRequest;
import org.gateway.paygate.vault.dto.response.TokenizeResponse;
import org.gateway.paygate.vault.service.VaultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/vault")
public class VaultController {
        private final VaultService vaultService;
        private final MerchantContext merchantContext;

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid  @RequestBody TokenizeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vaultService.tokenize(request,merchantContext.getMerchantId()));
    }
}

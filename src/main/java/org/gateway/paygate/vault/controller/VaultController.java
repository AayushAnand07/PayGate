package org.gateway.paygate.vault.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    UUID merchantId = UUID.fromString("3e40c301-bcef-4803-9f08-5998abe4365e"); //TODO: replace with merchant context
    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid  @RequestBody TokenizeRequest request) {
        // Implement the logic for tokenization here
        return ResponseEntity.status(HttpStatus.CREATED).body(vaultService.tokenize(request,merchantId));
    }
}

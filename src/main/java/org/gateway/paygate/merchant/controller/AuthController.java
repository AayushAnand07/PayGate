package org.gateway.paygate.merchant.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gateway.paygate.merchant.dto.request.MerchantSignupRequest;
import org.gateway.paygate.merchant.dto.response.MerchantResponse;
import org.gateway.paygate.merchant.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MerchantResponse> signup(@RequestBody @Valid MerchantSignupRequest request) {
       return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

}

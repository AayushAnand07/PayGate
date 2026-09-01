package org.gateway.paygate.payment.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gateway.paygate.merchant.security.MerchantContext;
import org.gateway.paygate.payment.dto.request.PaymentInitRequestDto;
import org.gateway.paygate.payment.dto.response.PaymentResponse;
import org.gateway.paygate.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("v1/payments")
@RestController
@RequiredArgsConstructor
public class PaymentController {
        private final PaymentService paymentService;
        private final MerchantContext merchantContext;

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentInitRequestDto request) {
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiate(merchantContext.getMerchantId(), request));
        }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(@Valid @RequestBody UUID paymentId) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(paymentService.capture(merchantContext.getMerchantId(), paymentId));
        }


}

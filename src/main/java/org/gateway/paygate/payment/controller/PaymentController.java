package org.gateway.paygate.payment.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        UUID merchantId = UUID.fromString("3e40c301-bcef-4803-9f08-5998abe4365e"); //TODO: replace with merchant context

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentInitRequestDto request) {
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiate(merchantId, request));
        }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(@Valid @RequestBody UUID paymentId) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(paymentService.capture(merchantId, paymentId));
        }


}

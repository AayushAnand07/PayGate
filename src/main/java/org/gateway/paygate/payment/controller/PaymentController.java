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
        UUID merchantId = UUID.fromString("41d82ee8-0efb-462e-bdf5-b498ed4100d5"); //TODO: replace with merchant context

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

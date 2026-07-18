package org.gateway.paygate.payment.controller;

import lombok.RequiredArgsConstructor;
import org.gateway.paygate.payment.dto.request.CreateOrderRequest;
import org.gateway.paygate.payment.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {


    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        // Implementation for creating an order
        return ResponseEntity.ok(new CreateOrderRequest());
    }
}

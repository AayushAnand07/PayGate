package org.gateway.paygate.payment.controller;

import lombok.RequiredArgsConstructor;
import org.gateway.paygate.payment.dto.request.CreateOrderRequest;
import org.gateway.paygate.payment.dto.response.OrderResponse;
import org.gateway.paygate.payment.service.impl.OrderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    UUID merchantId = UUID.fromString("3e40c301-bcef-4803-9f08-5998abe4365e"); //TODO: replace with merchant context
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        // Implementation for creating an order
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(merchantId,request));
    }
}

package org.gateway.paygate.payment.service;

import org.gateway.paygate.payment.dto.request.CreateOrderRequest;
import org.gateway.paygate.payment.dto.response.OrderResponse;
import org.gateway.paygate.payment.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(UUID merchantId, CreateOrderRequest request);
    OrderResponse getById(UUID merchantId, UUID orderId);
    OrderResponse cancelOrder(UUID merchantId, UUID orderId);
    List<PaymentResponse> listPayments(UUID merchantId, UUID orderId);


}

package org.gateway.paygate.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import org.gateway.paygate.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentInitRequestDto(
        @NotNull(message = "Order ID is required")
        UUID orderId,
        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,
        Map<String,Object> methodDetails


) {
}

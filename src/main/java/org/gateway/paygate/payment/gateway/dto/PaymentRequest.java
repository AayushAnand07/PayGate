package org.gateway.paygate.payment.gateway.dto;

import org.gateway.paygate.common.entity.Money;
import org.gateway.paygate.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;


public record PaymentRequest(
        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethod method,
        Map<String,Object> methodDetails

) {
}

package org.gateway.paygate.payment.dto.response;

import org.gateway.paygate.common.entity.Money;
import org.gateway.paygate.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record OrderResponse(
    UUID Id,
    UUID merchantId,
    String receipt, //Merchant Reference No.
    Money amount,
    OrderStatus status,
    Integer attempts,
    Map<String, Object> notes,
    LocalDateTime expiresAt,
    LocalDateTime createdAt
) {
}

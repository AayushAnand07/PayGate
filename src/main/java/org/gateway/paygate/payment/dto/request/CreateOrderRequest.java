package org.gateway.paygate.payment.dto.request;

import org.gateway.paygate.common.entity.Money;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(
        Money amount,
        String reciept,
        Map<String, Object> notes,
        LocalDateTime expiresAt

) {
}

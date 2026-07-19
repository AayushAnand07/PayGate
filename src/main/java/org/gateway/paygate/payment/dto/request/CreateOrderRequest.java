package org.gateway.paygate.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.gateway.paygate.common.entity.Money;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(
        @NotNull(message = "Amount is required")
        Money amount,
        @Size(max = 100)
        String reciept,
        Map<String, Object> notes,
        LocalDateTime expiresAt

) {
}

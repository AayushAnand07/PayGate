package org.gateway.paygate.payment.processor.dto;

import org.gateway.paygate.common.entity.Money;
import org.gateway.paygate.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentProcessorRequest(
        UUID processingID,
        UUID paymentId,
        PaymentMethod method,
         Money amount,
        String pan,
        String expiry,
        Map<String, Object> methodDetails
) {
        public static PaymentProcessorRequest card(Money amount, String pan, String expiry,Map<String, Object> methodDetails){
            return new PaymentProcessorRequest(UUID.randomUUID(), null, PaymentMethod.CARD, amount, pan, expiry, methodDetails);
        }


        public static PaymentProcessorRequest noncard(UUID paymentId, PaymentMethod method, Money amount,Map<String, Object> methodDetails){
            return new PaymentProcessorRequest(UUID.randomUUID(), paymentId, method, amount, null, null, methodDetails);
        }



}

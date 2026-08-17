package org.gateway.paygate.payment.processor;

import org.gateway.paygate.common.enums.PaymentMethod;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorRequest;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentProcessorRouter {
    private Map<PaymentMethod, PaymentProcessor> paymentProcessors;
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        PaymentProcessor paymentProcessor = paymentProcessors.get(request.method());
        if (paymentProcessor == null) {
            throw new IllegalArgumentException("No payment processor found for payment method: " + request.method());
        }
        return paymentProcessor.charge(request);
    }
}

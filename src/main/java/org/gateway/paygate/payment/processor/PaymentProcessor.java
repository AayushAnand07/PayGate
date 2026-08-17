package org.gateway.paygate.payment.processor;

import org.gateway.paygate.payment.processor.dto.PaymentProcessorRequest;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {
    PaymentProcessorResponse charge(PaymentProcessorRequest request);
    
}

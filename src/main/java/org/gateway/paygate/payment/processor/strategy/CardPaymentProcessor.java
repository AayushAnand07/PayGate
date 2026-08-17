package org.gateway.paygate.payment.processor.strategy;

import org.gateway.paygate.common.util.RandomizerUtil;
import org.gateway.paygate.payment.processor.PaymentProcessor;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorRequest;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;

public class CardPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        return null;

    }
}

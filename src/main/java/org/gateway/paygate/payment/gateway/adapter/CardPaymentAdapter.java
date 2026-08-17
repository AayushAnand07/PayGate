package org.gateway.paygate.payment.gateway.adapter;

import org.gateway.paygate.payment.gateway.PaymentAdapter;
import org.gateway.paygate.payment.gateway.dto.PaymentRequest;
import org.gateway.paygate.payment.gateway.dto.PaymentResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CardPaymentAdapter implements PaymentAdapter {
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}

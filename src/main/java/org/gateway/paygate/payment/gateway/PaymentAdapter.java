package org.gateway.paygate.payment.gateway;

import org.gateway.paygate.payment.gateway.dto.PaymentRequest;
import org.gateway.paygate.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);

    PaymentResult capture(UUID paymentId);
}

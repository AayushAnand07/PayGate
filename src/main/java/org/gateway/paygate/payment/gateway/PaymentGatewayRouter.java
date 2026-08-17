package org.gateway.paygate.payment.gateway;

import lombok.RequiredArgsConstructor;
import org.gateway.paygate.common.enums.PaymentMethod;
import org.gateway.paygate.payment.gateway.dto.PaymentRequest;
import org.gateway.paygate.payment.gateway.dto.PaymentResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter
{
    private final Map<PaymentMethod, PaymentAdapter> paymentAdapters;

   public PaymentResult initiate(PaymentRequest request){
        PaymentAdapter adapter = paymentAdapters.get(request.method());
        if (adapter != null) {
            adapter.initiate(request);
        } else throw new IllegalArgumentException("Unsupported payment method: " + request.method());
        return adapter.initiate(request);
    }

    public PaymentResult capture(PaymentMethod method, UUID paymentId) {
        PaymentAdapter adapter = paymentAdapters.get(method);
        if (adapter != null) {
            return adapter.capture(paymentId);
        } else {
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
    }
}

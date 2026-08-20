package org.gateway.paygate.payment.gateway.adapter;

import lombok.RequiredArgsConstructor;
import org.gateway.paygate.payment.gateway.PaymentAdapter;
import org.gateway.paygate.payment.gateway.dto.PaymentRequest;
import org.gateway.paygate.payment.gateway.dto.PaymentResult;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;
import org.gateway.paygate.vault.service.VaultService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {

    private final VaultService vaultService;
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        String token  = (String) request.methodDetails().get("token");
        PaymentProcessorResponse response = vaultService.charge(request.paymentId(),token,request.amount(),request.methodDetails());

        return switch (response) {
            case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
            case PaymentProcessorResponse.Failure failure -> new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
            case PaymentProcessorResponse.Pending pending -> new PaymentResult.Pending(pending.processorReference());
        };
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}

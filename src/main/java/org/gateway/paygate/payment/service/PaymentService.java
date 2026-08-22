package org.gateway.paygate.payment.service;

import jakarta.validation.Valid;
import org.gateway.paygate.payment.dto.request.PaymentInitRequestDto;
import org.gateway.paygate.payment.dto.response.PaymentResponse;

import java.util.UUID;


public interface PaymentService {
    PaymentResponse initiate(UUID merchantID, PaymentInitRequestDto request);

    PaymentResponse capture(UUID merchantId, @Valid UUID paymentId);

    void resolveAuthorization(UUID paymentId, boolean approve, String bankReference, String simBankErrorCode, String simulatedTransactionError);
}

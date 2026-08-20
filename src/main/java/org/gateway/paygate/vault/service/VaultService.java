package org.gateway.paygate.vault.service;

import org.gateway.paygate.common.entity.Money;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;
import org.gateway.paygate.vault.dto.request.TokenizeRequest;
import org.gateway.paygate.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID PaymentId,String token, Money amount, Map<String, Object> methodDetails);
}

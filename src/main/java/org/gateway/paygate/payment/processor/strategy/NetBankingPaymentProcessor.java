package org.gateway.paygate.payment.processor.strategy;

import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.util.RandomizerUtil;
import org.gateway.paygate.payment.processor.PaymentProcessor;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorRequest;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

         final String BANK_CODE_FAIL= "BANK_CODE_FAIL";


        String bankCode =  request.methodDetails() != null ?
                request.methodDetails().get("bank").toString() : null;

        if (BANK_CODE_FAIL.equals(bankCode)) {
            return new PaymentProcessorResponse.Failure("BANK_REJECTED", "Bank rejected the transaction registeration");
        }


        String processorRef = "NBK_PROCESSOR_" + RandomizerUtil.randomBase64(16);
        String redirectRef = "http://REDIRECT_BANK/" +processorRef;

        return new PaymentProcessorResponse.Pending(processorRef);

    }
}

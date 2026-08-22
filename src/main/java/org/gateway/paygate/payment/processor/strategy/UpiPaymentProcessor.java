package org.gateway.paygate.payment.processor.strategy;

import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.util.RandomizerUtil;
import org.gateway.paygate.payment.processor.PaymentProcessor;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorRequest;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpiPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        final String VPA_CODE_FAIL= "fail@okaxis";


        String vpaCode =  request.methodDetails() != null ?
                request.methodDetails().get("vpa").toString() : null;

        if (VPA_CODE_FAIL.equals(vpaCode)) {
            return new PaymentProcessorResponse.Failure("VPA_REJECTED", "VPA rejected the transaction registeration");
        }


        String processorRef = "UPI_PROCESSOR_" + RandomizerUtil.randomBase64(16);
        //String redirectRef = "http://REDIRECT_BANK/" +processorRef;

        return new PaymentProcessorResponse.Pending(processorRef );

    }
}

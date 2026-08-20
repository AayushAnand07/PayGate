package org.gateway.paygate.payment.processor.strategy;

import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.util.RandomizerUtil;
import org.gateway.paygate.payment.processor.PaymentProcessor;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorRequest;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardPaymentProcessor implements PaymentProcessor {

    public static final String PAN_CARD_DECLINED="4000000000000002";
    public static final String PAN_CARD_EXPIRED="4000000000000003";
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        String pan = request.pan();

        if(PAN_CARD_DECLINED.equals(pan)){
            log.warn("Card declined for PAN");
            return new PaymentProcessorResponse.Failure("CARD_DECLINED","The card was declined");
        }
        if(PAN_CARD_EXPIRED.equals(pan)){
            log.warn("Card has expired");
            return new PaymentProcessorResponse.Failure("CARD_EXPIRED","The card has expired");
        }
        String processorRef = "CARD_PROCESSOR_" + RandomizerUtil.randomBase64(16);
        return new PaymentProcessorResponse.Pending(processorRef);

    }
}

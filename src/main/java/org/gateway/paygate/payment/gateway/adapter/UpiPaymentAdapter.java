package org.gateway.paygate.payment.gateway.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.enums.PaymentMethod;
import org.gateway.paygate.payment.gateway.PaymentAdapter;
import org.gateway.paygate.payment.gateway.dto.PaymentRequest;
import org.gateway.paygate.payment.gateway.dto.PaymentResult;
import org.gateway.paygate.payment.processor.PaymentProcessorRouter;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorRequest;
import org.gateway.paygate.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class UpiPaymentAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        log.info("Initiating UPI payment for request: {}", request.paymentId());

        try {
            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.noncard(
                    request.paymentId(),
                    PaymentMethod.UPI,
                    request.amount(),
                    request.methodDetails());


            PaymentProcessorResponse paymentProcessorResponse =
                    paymentProcessorRouter.charge(paymentProcessorRequest);


            return switch (paymentProcessorResponse) {
                case PaymentProcessorResponse.Failure failure ->
                        new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
                case PaymentProcessorResponse.Pending pending ->
                        new PaymentResult.Pending(pending.processorReference());
                case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());

            };
        }
        catch (Exception e) {
            log.error("Error occurred while initiating UPI payment for request: {}", request.paymentId(), e);
            return new PaymentResult.Failure("UPI_FAILED", "An internal error occurred");
        }
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("UPI_REF");
    }

}

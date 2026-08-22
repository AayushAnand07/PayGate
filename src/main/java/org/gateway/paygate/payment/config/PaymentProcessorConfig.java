package org.gateway.paygate.payment.config;

import lombok.RequiredArgsConstructor;
import org.gateway.paygate.common.enums.PaymentMethod;
import org.gateway.paygate.payment.processor.PaymentProcessor;
import org.gateway.paygate.payment.processor.strategy.CardPaymentProcessor;
import org.gateway.paygate.payment.processor.strategy.NetBankingPaymentProcessor;
import org.gateway.paygate.payment.processor.strategy.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.smartcardio.Card;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {

    private final CardPaymentProcessor cardPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;
    private final UpiPaymentProcessor upiPaymentProcessor;

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessors() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentProcessor,
                PaymentMethod.NETBANKING, netBankingPaymentProcessor,
                PaymentMethod.UPI, upiPaymentProcessor
        );
    }
}

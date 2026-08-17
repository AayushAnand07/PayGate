package org.gateway.paygate.payment.config;

import org.gateway.paygate.common.enums.PaymentMethod;
import org.gateway.paygate.payment.processor.PaymentProcessor;
import org.gateway.paygate.payment.processor.strategy.CardPaymentProcessor;
import org.gateway.paygate.payment.processor.strategy.NetBankingPaymentProcessor;
import org.gateway.paygate.payment.processor.strategy.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public class PaymentProcessorConfig {

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessors() {
        return Map.of(
                PaymentMethod.CARD, new CardPaymentProcessor(),
                PaymentMethod.NETBANKING, new NetBankingPaymentProcessor(),
                PaymentMethod.UPI, new UpiPaymentProcessor()
        );
    }
}

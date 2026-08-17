package org.gateway.paygate.payment.config;

import lombok.RequiredArgsConstructor;
import org.gateway.paygate.common.enums.PaymentMethod;
import org.gateway.paygate.payment.gateway.PaymentAdapter;
import org.gateway.paygate.payment.gateway.adapter.CardPaymentAdapter;
import org.gateway.paygate.payment.gateway.adapter.NetBankingAdapter;
import org.gateway.paygate.payment.gateway.adapter.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;



@RequiredArgsConstructor
@Configuration
public class PaymentAdapterConfig {

    private final NetBankingAdapter netBankingAdapter;
    private final CardPaymentAdapter cardPaymentAdapter;
    private final UpiPaymentAdapter upiPaymentAdapter;

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap(){
        return Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.NETBANKING, netBankingAdapter,
                PaymentMethod.UPI , upiPaymentAdapter
        );
    }
}

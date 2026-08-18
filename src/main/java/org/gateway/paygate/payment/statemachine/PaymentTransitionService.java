package org.gateway.paygate.payment.statemachine;


import lombok.RequiredArgsConstructor;
import org.gateway.paygate.common.enums.PaymentActor;
import org.gateway.paygate.common.enums.PaymentEvent;
import org.gateway.paygate.common.enums.PaymentStatus;
import org.gateway.paygate.payment.entity.Payment;
import org.gateway.paygate.payment.entity.PaymentTransitionLog;
import org.gateway.paygate.payment.repository.PaymentTransitionLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus applyTransition(Payment payment, PaymentEvent event) {
       PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);
       payment.setStatus(next);
        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .event(event)
                .toStatus(next)
                .actor(PaymentActor.SYSTEM)
                .occurredAt(LocalDateTime.now())
                .build();

        paymentTransitionLogRepository.save(log);
        return next;
    }
}

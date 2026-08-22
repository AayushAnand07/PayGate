package org.gateway.paygate.payment.simulator;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.gateway.paygate.common.enums.ChaosMode;
import org.gateway.paygate.common.enums.PaymentStatus;
import org.gateway.paygate.common.util.RandomizerUtil;
import org.gateway.paygate.payment.entity.Payment;
import org.gateway.paygate.payment.repository.PaymentRepository;
import org.gateway.paygate.payment.service.PaymentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankCallbackSimulator {

    private final PaymentRepository paymentRepository;
    private  final PaymentService paymentService;
    private final SimulatorConfig simulatorConfig;

    @Scheduled(fixedDelayString = "${simulator.poll-interval-ms:5000}")
    public void processCallbacks(){

        LocalDateTime globalWindow = LocalDateTime.now().minusSeconds(1);
        List<Payment> paymentsList = paymentRepository.findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING,globalWindow);
        log.info("Simulating payments for {} payments",paymentsList.size());
        if(paymentsList.isEmpty()){
            log.info("No payments found for callback simulation");
            return;
        }
        for(Payment payment:paymentsList){
            log.info("Simulating callback for paymentId: {}",payment.getId());
            try {
                simulateBankCallback(payment);
            } catch (Exception e) {
                log.error("Error simulating callback for paymentId: {}",payment.getId(),e);
            }
        }
    }


    private void simulateBankCallback(Payment payment) {
        SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig = simulatorConfig.configFor(payment.getMethod());
        LocalDateTime dueAt = dueAt(payment, methodSimulatorConfig);
        if (LocalDateTime.now().isBefore(dueAt)) {
            log.info("Payment {} is not due for callback yet. Due at: {}", payment.getId(), dueAt);
            return;
        }
        ChaosMode chaosMode = simulatorConfig.getChaosMode();
        switch (chaosMode) {
            case SUCCESS -> resolve(payment, true);
            case FAILURE -> resolve(payment, false);
            case TIMEOUT -> log.debug("Simulating timeout for payment {}. No callback will be sent.", payment.getId());
            case NORMAL , SLOW-> {
                int randomValue = Math.abs(payment.getId().hashCode()) % 100;
                if (randomValue <= methodSimulatorConfig.getSucessRate()) {
                    resolve(payment, true);
                } else {
                    resolve(payment, false);
                }
            }
        }
    }

    private void resolve(Payment payment , boolean approve){
        if(approve) {
            String bankRef = "SIM_BANK_REF"+ RandomizerUtil.randomBase64(8);
            paymentService.resolveAuthorization(payment.getId(), true,bankRef, null,null);

        }
        paymentService.resolveAuthorization(payment.getId(), false,null, "SIM_BANK_ERROR_CODE","Simulted transaction error");


    }

    private LocalDateTime dueAt(Payment payment, SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig){
        int range = methodSimulatorConfig.getMaxDelaySeconds() - methodSimulatorConfig.getMinDelaySeconds();
        int delaySeconds = methodSimulatorConfig.getMaxDelaySeconds() + Math.abs(payment.getId().hashCode()) % (range + 1);
        if(simulatorConfig.getChaosMode() == ChaosMode.SLOW){
            delaySeconds *= 2;
        }
        return payment.getCreatedAt().plusSeconds(delaySeconds);
    }

}

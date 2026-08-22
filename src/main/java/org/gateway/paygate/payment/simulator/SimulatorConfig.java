package org.gateway.paygate.payment.simulator;

import lombok.Getter;
import lombok.Setter;
import org.gateway.paygate.common.enums.ChaosMode;
import org.gateway.paygate.common.enums.PaymentMethod;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "payment.simulator")
@Setter
@Getter
public class SimulatorConfig {

    private Integer pollIntervalMs = 2000;
    private ChaosMode chaosMode = ChaosMode.NORMAL;
    private Map<String, MethodSimulatorConfig> methods =  new HashMap<>();

    public SimulatorConfig.MethodSimulatorConfig configFor(PaymentMethod paymentMethod){
        return methods.getOrDefault(paymentMethod.name(), new MethodSimulatorConfig());
    }



    @Getter
    @Setter
    public static class MethodSimulatorConfig {
        private Integer minDelaySeconds = 1;
        private Integer maxDelaySeconds = 5;
        private Integer sucessRate = 80;
    }


}


package org.gateway.paygate.common.audit;

import lombok.RequiredArgsConstructor;
import org.gateway.paygate.merchant.security.MerchantContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final MerchantContext merchantContext;
    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            String keyId = merchantContext.getKeyId();
            if (keyId != null && !keyId.isBlank()) {
                return Optional.of(keyId);
            }
            if (merchantContext.getMerchantId() != null) {
                return Optional.of(merchantContext.getMerchantId().toString());
            }
        }
        catch (Exception e){

        }
        return Optional.of("SYSTEM");
    }
}

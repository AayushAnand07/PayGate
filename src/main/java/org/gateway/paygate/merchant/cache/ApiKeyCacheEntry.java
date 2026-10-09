package org.gateway.paygate.merchant.cache;

import org.gateway.paygate.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyCacheEntry(
        String keyId,
        String keySecretHash,
        String previousSecretHash,
        LocalDateTime gracePeriodExpiresAt,
        UUID merchantId,
        Environment environment,
        boolean enabled

) {
    public boolean isInGracePeriod(){
        return gracePeriodExpiresAt!=null && LocalDateTime.now().isBefore(gracePeriodExpiresAt);
    }

}

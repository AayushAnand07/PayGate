package org.gateway.paygate.merchant.dto.response;

import org.gateway.paygate.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}

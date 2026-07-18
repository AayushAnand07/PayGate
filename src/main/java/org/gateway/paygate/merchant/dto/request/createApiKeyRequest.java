package org.gateway.paygate.merchant.dto.request;

import org.gateway.paygate.common.enums.Environment;

public record createApiKeyRequest (
        Environment environment
){

}

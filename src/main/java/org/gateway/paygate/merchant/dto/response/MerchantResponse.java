package org.gateway.paygate.merchant.dto.response;


import org.gateway.paygate.common.enums.BusinessType;
import org.gateway.paygate.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
    UUID id,
    String name,
    String email,
    String businessName,
    BusinessType businessType,
    MerchantStatus merchantStatus

) {
}

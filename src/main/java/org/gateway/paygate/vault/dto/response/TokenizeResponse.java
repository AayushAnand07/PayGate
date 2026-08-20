package org.gateway.paygate.vault.dto.response;

import org.gateway.paygate.common.enums.CardBrand;


public record TokenizeResponse (

      String token,
      String last4,
      CardBrand cardBrand,
      Integer expiryMonth,
      Integer expiryYear
){ }



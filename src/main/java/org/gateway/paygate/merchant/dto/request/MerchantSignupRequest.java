package org.gateway.paygate.merchant.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.gateway.paygate.common.enums.BusinessType;

public record MerchantSignupRequest(


    @NotNull(message = "Email is required")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    String email,

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    @NotNull(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    String password,

    @NotNull(message = "Business name is required")
    @Size(min = 2, max = 100, message = "Business name must be between 2 and 100 characters")
    String businessName,

    BusinessType businessType
) {
}

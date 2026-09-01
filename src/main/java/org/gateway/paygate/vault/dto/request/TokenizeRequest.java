package org.gateway.paygate.vault.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.gateway.paygate.vault.validation.ExpiryYear;
import org.hibernate.validator.constraints.LuhnCheck;

import java.util.UUID;

public record TokenizeRequest(

        @NotBlank(message = "PAN is required")
        @LuhnCheck(message = "PAN is invalid")
        @Pattern(
                regexp = "^[0-9]{13,19}$",
                message = "PAN must be between 13 and 19 digits"
        )
        String pan,

        @NotBlank(message = "CVV is required")
        @Pattern(
                regexp = "^[0-9]{3,4}$",
                message = "CVV must be 3 or 4 digits"
        )
        String cvv,

        @NotNull(message = "Expiry month is required")
        @Min(value = 1, message = "Expiry month is invalid")
        @Max(value = 12, message = "Expiry month is invalid")
        Integer expiryMonth,

        @NotNull(message = "Expiry year is required")
        @ExpiryYear
        Integer expiryYear,

        UUID customerId,

        @Size(min = 3, message = "Card name should have at least 3 characters")
        String cardHolderName

) {}
package com.configquote.configquote_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(

		@NotBlank(message = "SKU is required") @Size(max = 50) String sku,

		@NotBlank(message = "Product name is required") @Size(max = 200) String name,

		String description,

		@NotNull(message = "Base price is required") @DecimalMin(value = "0.00", message = "Base price cannot be negative") @Digits(integer = 10, fraction = 2) BigDecimal basePrice

) {
}

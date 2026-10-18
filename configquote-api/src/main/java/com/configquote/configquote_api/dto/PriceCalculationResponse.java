package com.configquote.configquote_api.dto;

import java.math.BigDecimal;

public record PriceCalculationResponse(
    Long productId,
    String productName,
    BigDecimal basePrice,
    BigDecimal optionsPrice,
    BigDecimal totalPrice
) {}

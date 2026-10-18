package com.configquote.configquote_api.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record PriceCalculationRequest(@NotNull List<@NotNull Long> selectedValueIds) {
}

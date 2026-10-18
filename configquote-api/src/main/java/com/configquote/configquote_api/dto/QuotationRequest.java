package com.configquote.configquote_api.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record QuotationRequest(

		@NotNull Long customerId,

		@NotEmpty List<@NotNull @Valid ItemRequest> items

) {
	public record ItemRequest(@NotNull Long productId, @NotNull @Min(1) Integer quantity,
			@NotNull List<@NotNull Long> selectedValueIds) {
	}
}

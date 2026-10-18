package com.configquote.configquote_api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record QuotationResponse(Long id, String quoteNumber, Long customerId, String status, BigDecimal totalAmount,
		OffsetDateTime createdAt, List<ItemResponse> items) {
	public record ItemResponse(Long id, Long productId, String productName, Integer quantity, BigDecimal unitPrice,
			BigDecimal lineTotal, List<SelectionResponse> selections) {
	}

	public record SelectionResponse(String optionName, String selectedValue, BigDecimal additionalPrice) {
	}
}

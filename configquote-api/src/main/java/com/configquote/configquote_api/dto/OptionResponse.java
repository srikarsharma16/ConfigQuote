package com.configquote.configquote_api.dto;

import java.math.BigDecimal;
import java.util.List;

public record OptionResponse(Long id, String name, boolean required, List<ValueResponse> values) {
	public record ValueResponse(Long id, String value, BigDecimal additionalPrice) {
	}
}

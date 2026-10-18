package com.configquote.configquote_api.dto;

import jakarta.validation.constraints.NotNull;

public record QuotationStatusRequest(@NotNull Status status) {
	public enum Status {
		DRAFT, SENT, ACCEPTED, REJECTED
	}
}

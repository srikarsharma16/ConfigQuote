package com.configquote.configquote_api.dto;

import java.time.OffsetDateTime;

public record CustomerResponse(Long id, String customerCode, String companyName, String contactName, String email,
		String phone, String address, OffsetDateTime createdAt) {
}

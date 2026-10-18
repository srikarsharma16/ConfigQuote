package com.configquote.configquote_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(@NotBlank @Size(max = 100) String username,
		@NotBlank @Size(min = 12, max = 128) String password, @NotNull Role role) {
	public enum Role {
		ADMIN, SALES, VIEWER
	}
}

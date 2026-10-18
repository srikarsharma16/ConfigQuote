package com.configquote.configquote_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequest(

		@NotBlank(message = "Customer code is required") @Size(max = 30) String customerCode,

		@NotBlank(message = "Company name is required") @Size(max = 200) String companyName,

		@NotBlank(message = "Contact name is required") @Size(max = 150) String contactName,

		@NotBlank(message = "Email is required") @Email(message = "Enter a valid email address") @Size(max = 255) String email,

		@Size(max = 30) String phone,

		String address

) {
}

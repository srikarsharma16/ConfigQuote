
package com.configquote.configquote_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI configQuoteOpenAPI() {

		String schemeName = "bearerAuth";

		return new OpenAPI()
				.info(new Info().title("ConfigQuote API").version("v1.0")
						.description("Engineering Product Configuration " + "and Quotation Management API"))
				.addSecurityItem(new SecurityRequirement().addList(schemeName)).schemaRequirement(schemeName,
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
	}
}

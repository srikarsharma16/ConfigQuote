package com.configquote.configquote_api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.configquote.configquote_api.dto.*;
import com.configquote.configquote_api.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	@Operation(summary = "Login and obtain JWT")
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {

		return authService.login(request);
	}

	@GetMapping("/me")
	@Operation(summary = "Get current user")
	public UserResponse me(Authentication authentication) {

		return authService.getCurrentUser(authentication.getName());
	}

	@PostMapping("/users")
	@Operation(summary = "Create a user (ADMIN only)")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {

		UserResponse created = authService.createUser(request);

		return ResponseEntity.created(URI.create("/api/auth/users/" + created.id())).body(created);
	}
}

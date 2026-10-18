package com.configquote.configquote_api.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.configquote.configquote_api.dto.*;
import com.configquote.configquote_api.entity.AppUser;
import com.configquote.configquote_api.repository.AppUserRepository;
import com.configquote.configquote_api.security.JwtService;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final AppUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(AuthenticationManager authenticationManager, AppUserRepository userRepository,
			PasswordEncoder passwordEncoder, JwtService jwtService) {

		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
		} catch (AuthenticationException ex) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
		}

		AppUser user = userRepository.findByUsername(request.username())
				.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

		return new LoginResponse(jwtService.generateToken(user), "Bearer", jwtService.getExpiresInSeconds());
	}

	@Transactional
	public UserResponse createUser(CreateUserRequest request) {

		if (userRepository.existsByUsername(request.username())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
		}

		AppUser user = new AppUser();
		user.setUsername(request.username());
		user.setPasswordHash(passwordEncoder.encode(request.password()));
		user.setRole(request.role().name());
		user.setEnabled(true);

		AppUser saved = userRepository.saveAndFlush(user);

		return toResponse(saved);
	}

	@Transactional(readOnly = true)
	public UserResponse getCurrentUser(String username) {

		AppUser user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

		if (!user.isEnabled()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User disabled");
		}

		return toResponse(user);
	}

	private UserResponse toResponse(AppUser user) {
		return new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.isEnabled());
	}
}

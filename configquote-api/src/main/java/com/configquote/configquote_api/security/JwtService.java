package com.configquote.configquote_api.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import com.configquote.configquote_api.entity.AppUser;

@Service
public class JwtService {

	private final JwtEncoder jwtEncoder;
	private final long expirationMinutes;

	public JwtService(JwtEncoder jwtEncoder, @Value("${app.jwt.expiration-minutes:60}") long expirationMinutes) {

		this.jwtEncoder = jwtEncoder;
		this.expirationMinutes = expirationMinutes;
	}

	public String generateToken(AppUser user) {

		Instant now = Instant.now();

		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("configquote-api").subject(user.getUsername()).issuedAt(now)
				.expiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES)).claim("roles", List.of(user.getRole()))
				.build();

		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}

	public long getExpiresInSeconds() {
		return expirationMinutes * 60;
	}
}

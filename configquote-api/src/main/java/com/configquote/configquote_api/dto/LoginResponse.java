package com.configquote.configquote_api.dto;

public record LoginResponse(
    String accessToken,
    String tokenType,
    long expiresIn
) {}

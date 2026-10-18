package com.configquote.configquote_api.dto;

public record UserResponse(
    Long id,
    String username,
    String role,
    boolean enabled
) {}

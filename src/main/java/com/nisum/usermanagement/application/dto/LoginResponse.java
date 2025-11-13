package com.nisum.usermanagement.application.dto;

import java.time.LocalDateTime;

public record LoginResponse(
        String token,
        LocalDateTime expiresAt
) {}

package com.nisum.usermanagement.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        LocalDateTime created,
        LocalDateTime modified,
        LocalDateTime lastLogin,
        String token,
        boolean isactive,
        String name,
        String email,
        List<PhoneResponse> phones
) {}

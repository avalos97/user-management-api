package com.nisum.usermanagement.domain.service;

import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PasswordPolicy {
    private final Pattern passwordPattern;

    public void validate(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (!passwordPattern.matcher(rawPassword).matches()) {
            throw new IllegalArgumentException("Formato de contraseña inválido");
        }
    }
}

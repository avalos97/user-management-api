package com.nisum.usermanagement.domain.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class JwtToken {
    private final String value;

    private JwtToken(String value) {
        this.value = value;
    }

    public static JwtToken of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El token no puede estar vacío");
        }
        return new JwtToken(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

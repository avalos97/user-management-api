package com.nisum.usermanagement.domain.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public class Email {
    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email of(String value, Pattern emailPattern) {
        if (!emailPattern.matcher(value).matches()) {
            throw new IllegalArgumentException("Formato de correo inválido");
        }
        return new Email(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

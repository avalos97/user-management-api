package com.nisum.usermanagement.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class PasswordPolicyTest {

    private PasswordPolicy passwordPolicy;

    @BeforeEach
    void setUp() {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
        passwordPolicy = new PasswordPolicy(pattern);
    }

    @Test
    void shouldValidateCorrectPassword() {
        assertDoesNotThrow(() -> passwordPolicy.validate("Password123!"));
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        assertThrows(IllegalArgumentException.class, 
            () -> passwordPolicy.validate(null));
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsBlank() {
        assertThrows(IllegalArgumentException.class, 
            () -> passwordPolicy.validate(""));
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatchPattern() {
        assertThrows(IllegalArgumentException.class, 
            () -> passwordPolicy.validate("weak"));
    }

    @Test
    void shouldThrowExceptionWhenPasswordMissingUppercase() {
        assertThrows(IllegalArgumentException.class, 
            () -> passwordPolicy.validate("password123!"));
    }

    @Test
    void shouldThrowExceptionWhenPasswordMissingNumber() {
        assertThrows(IllegalArgumentException.class, 
            () -> passwordPolicy.validate("Password!"));
    }

    @Test
    void shouldThrowExceptionWhenPasswordMissingSpecialChar() {
        assertThrows(IllegalArgumentException.class, 
            () -> passwordPolicy.validate("Password123"));
    }
}

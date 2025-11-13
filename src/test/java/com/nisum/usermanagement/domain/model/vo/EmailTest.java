package com.nisum.usermanagement.domain.model.vo;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    private final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Test
    void shouldCreateValidEmail() {
        Email email = Email.of("test@example.com", emailPattern);
        
        assertNotNull(email);
        assertEquals("test@example.com", email.getValue());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        assertThrows(IllegalArgumentException.class, 
            () -> Email.of("", emailPattern));
    }

    @Test
    void shouldThrowExceptionWhenEmailFormatIsInvalid() {
        assertThrows(IllegalArgumentException.class, 
            () -> Email.of("invalid-email", emailPattern));
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        Email email1 = Email.of("test@example.com", emailPattern);
        Email email2 = Email.of("test@example.com", emailPattern);
        
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }
}

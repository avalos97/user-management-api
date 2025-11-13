package com.nisum.usermanagement.domain.model.aggregate;

import com.nisum.usermanagement.domain.model.vo.Email;
import com.nisum.usermanagement.domain.model.vo.JwtToken;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Test
    void shouldCreateUserWithFactoryMethod() {
        UUID id = UUID.randomUUID();
        Email email = Email.of("test@example.com", emailPattern);
        JwtToken token = JwtToken.of("dummy-token");
        LocalDateTime now = LocalDateTime.now();
        List<Phone> phones = new ArrayList<>();
        phones.add(Phone.create("1234567", "1", "57"));

        User user = User.create(id, "Test User", email, "hashedPassword", now, phones, token);

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals(email, user.getEmail());
        assertEquals("hashedPassword", user.getPasswordHash());
        assertEquals(now, user.getCreated());
        assertEquals(now, user.getModified());
        assertEquals(now, user.getLastLogin());
        assertEquals(token, user.getToken());
        assertTrue(user.isActive());
        assertEquals(1, user.getPhones().size());
    }

    @Test
    void shouldRegisterLoginAndUpdateTimestamps() {
        UUID id = UUID.randomUUID();
        Email email = Email.of("test@example.com", emailPattern);
        JwtToken token = JwtToken.of("old-token");
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        List<Phone> phones = new ArrayList<>();

        User user = User.create(id, "Test User", email, "hashedPassword", createdAt, phones, token);

        LocalDateTime loginTime = LocalDateTime.now();
        JwtToken newToken = JwtToken.of("new-token");
        user.registerLogin(loginTime, newToken);

        assertEquals(loginTime, user.getLastLogin());
        assertEquals(loginTime, user.getModified());
        assertEquals(newToken, user.getToken());
    }

    @Test
    void shouldReturnUnmodifiablePhoneList() {
        UUID id = UUID.randomUUID();
        Email email = Email.of("test@example.com", emailPattern);
        JwtToken token = JwtToken.of("dummy-token");
        LocalDateTime now = LocalDateTime.now();
        List<Phone> phones = new ArrayList<>();

        User user = User.create(id, "Test User", email, "hashedPassword", now, phones, token);

        assertThrows(UnsupportedOperationException.class, 
            () -> user.getPhones().add(Phone.create("999", "2", "58")));
    }
}

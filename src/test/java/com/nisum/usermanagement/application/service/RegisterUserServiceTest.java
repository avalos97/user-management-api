package com.nisum.usermanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nisum.usermanagement.application.dto.PhoneRequest;
import com.nisum.usermanagement.application.dto.UserRequest;
import com.nisum.usermanagement.application.dto.UserResponse;
import com.nisum.usermanagement.application.exception.DuplicateEmailException;
import com.nisum.usermanagement.domain.model.aggregate.User;
import com.nisum.usermanagement.domain.model.vo.Email;
import com.nisum.usermanagement.domain.model.vo.JwtToken;
import com.nisum.usermanagement.domain.repository.UserRepository;
import com.nisum.usermanagement.domain.service.PasswordPolicy;
import com.nisum.usermanagement.infrastructure.security.JwtService;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private PasswordPolicy passwordPolicy;
    private Pattern emailPattern;
    private RegisterUserService registerUserService;

    @BeforeEach
    void setUp() {
        emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
        passwordPolicy = new PasswordPolicy(passwordPattern);
        
        registerUserService = new RegisterUserService(
            userRepository,
            passwordPolicy,
            passwordEncoder,
            jwtService,
            emailPattern
        );
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRequest request = createUserRequest("Juan Rodriguez", "juan@rodriguez.org", "Password123!", "1234567");
        
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(jwtService.generateToken(anyString())).thenReturn("jwt-token");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = registerUserService.register(request);

        assertNotNull(response);
        assertEquals("Juan Rodriguez", response.name());
        assertEquals("juan@rodriguez.org", response.email());
        assertEquals("jwt-token", response.token());
        assertTrue(response.isactive());
        assertEquals(1, response.phones().size());
        
        verify(userRepository).findByEmail(any(Email.class));
        verify(passwordEncoder).encode("Password123!");
        verify(jwtService).generateToken("juan@rodriguez.org");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserRequest request = createUserRequest("Juan Rodriguez", "juan@rodriguez.org", "Password123!", "1234567");
        
        User existingUser = User.create(
            UUID.randomUUID(),
            "Existing",
            Email.of("juan@rodriguez.org", emailPattern),
            "hash",
            LocalDateTime.now(),
            new ArrayList<>(),
            JwtToken.of("token")
        );
        
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(existingUser));

        assertThrows(DuplicateEmailException.class, () -> registerUserService.register(request));
        
        verify(userRepository).findByEmail(any(Email.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenPasswordInvalid() {
        UserRequest request = createUserRequest("Juan Rodriguez", "juan@rodriguez.org", "weak", "1234567");

        assertThrows(IllegalArgumentException.class, () -> registerUserService.register(request));
        
        verify(userRepository, never()).findByEmail(any(Email.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailFormatInvalid() {
        UserRequest request = createUserRequest("Juan Rodriguez", "invalid-email", "Password123!", "1234567");

        assertThrows(IllegalArgumentException.class, () -> registerUserService.register(request));
        
        verify(userRepository, never()).findByEmail(any(Email.class));
        verify(userRepository, never()).save(any(User.class));
    }

    private UserRequest createUserRequest(String name, String email, String password, String phoneNumber) {
        List<PhoneRequest> phones = new ArrayList<>();
        phones.add(new PhoneRequest(phoneNumber, "1", "57"));
        return new UserRequest(name, email, password, phones);
    }
}

package com.nisum.usermanagement.application.service;

import com.nisum.usermanagement.application.dto.LoginRequest;
import com.nisum.usermanagement.application.dto.LoginResponse;
import com.nisum.usermanagement.application.exception.UserNotFoundException;
import com.nisum.usermanagement.domain.model.aggregate.User;
import com.nisum.usermanagement.domain.model.vo.Email;
import com.nisum.usermanagement.domain.model.vo.JwtToken;
import com.nisum.usermanagement.domain.repository.UserRepository;
import com.nisum.usermanagement.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Pattern emailPattern;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Email email = Email.of(request.getEmail(), emailPattern);
        
        User user = userRepository.findByEmail(email)
                .filter(User::isActive)
                .orElseThrow(() -> new UserNotFoundException(email.getValue()));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        
        String tokenValue = jwtService.generateToken(user.getEmail().getValue());
        JwtToken newToken = JwtToken.of(tokenValue);
        LocalDateTime now = LocalDateTime.now();
        
        user.registerLogin(now, newToken);
        userRepository.save(user);
        
        LocalDateTime expiresAt = jwtService.getExpirationDate(tokenValue);
        
        return new LoginResponse(tokenValue, expiresAt);
    }
}

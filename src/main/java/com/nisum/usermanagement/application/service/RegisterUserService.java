package com.nisum.usermanagement.application.service;

import com.nisum.usermanagement.application.dto.UserRequest;
import com.nisum.usermanagement.application.dto.UserResponse;
import com.nisum.usermanagement.application.exception.DuplicateEmailException;
import com.nisum.usermanagement.application.mapper.UserMapper;
import com.nisum.usermanagement.domain.model.aggregate.Phone;
import com.nisum.usermanagement.domain.model.aggregate.User;
import com.nisum.usermanagement.domain.model.vo.Email;
import com.nisum.usermanagement.domain.model.vo.JwtToken;
import com.nisum.usermanagement.domain.repository.UserRepository;
import com.nisum.usermanagement.domain.service.PasswordPolicy;
import com.nisum.usermanagement.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RegisterUserService {
    private final UserRepository userRepository;
    private final PasswordPolicy passwordPolicy;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Pattern emailPattern;

    @Transactional
    public UserResponse register(UserRequest request) {
        passwordPolicy.validate(request.getPassword());
        
        Email email = Email.of(request.getEmail(), emailPattern);
        
        userRepository.findByEmail(email)
                .ifPresent(u -> {
                    throw new DuplicateEmailException("El correo ya se encuentra registrado");
                });
        
        String passwordHash = passwordEncoder.encode(request.getPassword());
        
        String tokenValue = jwtService.generateToken(request.getEmail());
        JwtToken token = JwtToken.of(tokenValue);
        
        List<Phone> phones = UserMapper.toPhoneDomain(request.getPhones());
        
        User user = User.create(
                UUID.randomUUID(),
                request.getName(),
                email,
                passwordHash,
                LocalDateTime.now(),
                phones,
                token
        );
        
        User savedUser = userRepository.save(user);
        
        return UserMapper.toUserResponse(savedUser);
    }
}

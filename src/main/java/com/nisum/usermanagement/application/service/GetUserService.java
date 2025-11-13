package com.nisum.usermanagement.application.service;

import com.nisum.usermanagement.application.dto.UserResponse;
import com.nisum.usermanagement.application.exception.UserNotFoundException;
import com.nisum.usermanagement.application.mapper.UserMapper;
import com.nisum.usermanagement.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toUserResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}

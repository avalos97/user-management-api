package com.nisum.usermanagement.domain.repository;

import com.nisum.usermanagement.domain.model.aggregate.User;
import com.nisum.usermanagement.domain.model.vo.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(Email email);
    Optional<User> findById(UUID id);
    User save(User user);
}

package com.nisum.usermanagement.infrastructure.persistence.adapter;

import com.nisum.usermanagement.domain.model.aggregate.Phone;
import com.nisum.usermanagement.domain.model.aggregate.User;
import com.nisum.usermanagement.domain.model.vo.Email;
import com.nisum.usermanagement.domain.model.vo.JwtToken;
import com.nisum.usermanagement.domain.repository.UserRepository;
import com.nisum.usermanagement.infrastructure.persistence.entity.PhoneEntity;
import com.nisum.usermanagement.infrastructure.persistence.entity.UserEntity;
import com.nisum.usermanagement.infrastructure.persistence.jpa.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryJpaAdapter implements UserRepository {
    private final SpringDataUserRepository springDataUserRepository;
    private final Pattern emailPattern;

    @Override
    public Optional<User> findByEmail(Email email) {
        return springDataUserRepository.findByEmail(email.getValue())
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springDataUserRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = springDataUserRepository.save(entity);
        return toDomain(saved);
    }

    private User toDomain(UserEntity entity) {
        List<Phone> phones = entity.getPhones().stream()
                .map(pe -> new Phone(pe.getId(), pe.getNumber(), pe.getCitycode(), pe.getCountrycode()))
                .collect(Collectors.toList());

        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(Email.of(entity.getEmail(), emailPattern))
                .passwordHash(entity.getPassword())
                .created(entity.getCreated())
                .modified(entity.getModified())
                .lastLogin(entity.getLastLogin())
                .token(JwtToken.of(entity.getToken()))
                .isActive(entity.isActive())
                .phones(phones)
                .build();
    }

    private UserEntity toEntity(User user) {
        UserEntity userEntity = UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail().getValue())
                .password(user.getPasswordHash())
                .created(user.getCreated())
                .modified(user.getModified())
                .lastLogin(user.getLastLogin())
                .token(user.getToken().getValue())
                .isActive(user.isActive())
                .phones(new ArrayList<>())
                .build();

        user.getPhones().forEach(phone -> {
            PhoneEntity phoneEntity = new PhoneEntity();
            phoneEntity.setId(phone.getId());
            phoneEntity.setNumber(phone.getNumber());
            phoneEntity.setCitycode(phone.getCitycode());
            phoneEntity.setCountrycode(phone.getCountrycode());
            userEntity.addPhone(phoneEntity);
        });

        return userEntity;
    }
}

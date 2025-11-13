package com.nisum.usermanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime modified;

    @Column(name = "last_login", nullable = false)
    private LocalDateTime lastLogin;

    @Column(nullable = false, length = 1024)
    private String token;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneEntity> phones = new ArrayList<>();

    public void addPhone(PhoneEntity phone) {
        phones.add(phone);
        phone.setUser(this);
    }
}

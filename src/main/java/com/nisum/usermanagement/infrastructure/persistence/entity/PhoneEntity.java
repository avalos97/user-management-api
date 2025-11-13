package com.nisum.usermanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "phones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 50)
    private String number;

    @Column(nullable = false, length = 10)
    private String citycode;

    @Column(nullable = false, length = 10)
    private String countrycode;
}

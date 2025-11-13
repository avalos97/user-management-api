package com.nisum.usermanagement.domain.model.aggregate;

import com.nisum.usermanagement.domain.model.vo.Email;
import com.nisum.usermanagement.domain.model.vo.JwtToken;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
public class User {
    private UUID id;
    private String name;
    private Email email;
    private String passwordHash;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastLogin;
    private JwtToken token;
    
    @Builder.Default
    private boolean isActive = true;
    
    @Builder.Default
    private List<Phone> phones = new ArrayList<>();

    /**
     * Factory Method para crear un nuevo usuario registrado.
     * método para lógica de negocio (capa Domain)
     * @throws NullPointerException si algún parámetro requerido es null
     */
    public static User create(
            UUID id,
            String name,
            Email email,
            String passwordHash,
            LocalDateTime now,
            List<Phone> phones,
            JwtToken token
    ) {
        Objects.requireNonNull(id, "User id cannot be null");
        Objects.requireNonNull(name, "User name cannot be null");
        Objects.requireNonNull(email, "User email cannot be null");
        Objects.requireNonNull(passwordHash, "User password hash cannot be null");
        Objects.requireNonNull(now, "Timestamp cannot be null");
        Objects.requireNonNull(token, "User token cannot be null");
        
        if (name.isBlank()) {
            throw new IllegalArgumentException("User name cannot be blank");
        }
        
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .passwordHash(passwordHash)
                .created(now)
                .modified(now)
                .lastLogin(now)
                .token(token)
                .isActive(true)
                .phones(phones != null ? new ArrayList<>(phones) : new ArrayList<>())
                .build();
    }

    public List<Phone> getPhones() {
        return Collections.unmodifiableList(phones);
    }

    /**
     * Registra un nuevo login del usuario.
     * Actualiza el token JWT, la fecha de último login y la fecha de modificación.
     * 
     * @param now timestamp del login
     * @param newToken nuevo token JWT generado
     */
    public void registerLogin(LocalDateTime now, JwtToken newToken) {
        Objects.requireNonNull(now, "Login timestamp cannot be null");
        Objects.requireNonNull(newToken, "New token cannot be null");
        
        this.lastLogin = now;
        this.token = newToken;
        this.modified = now;
    }

}

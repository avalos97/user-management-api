package com.nisum.usermanagement.application.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(UUID id) {
        super("Usuario con ID " + id + " no encontrado");
    }
    
    public UserNotFoundException(String email) {
        super("Usuario con email '" + email + "' no encontrado");
    }
}

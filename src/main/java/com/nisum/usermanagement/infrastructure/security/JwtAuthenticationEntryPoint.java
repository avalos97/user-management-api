package com.nisum.usermanagement.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador personalizado para errores de autenticación.
 * Devuelve 401 Unauthorized cuando:
 * - No hay token JWT presente
 * - El token es inválido
 * - El token ha expirado
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        
        log.warn("Intento de acceso no autorizado: {} - {}", 
                request.getRequestURI(), 
                authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", "Autenticación requerida. Token JWT inválido o ausente.");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

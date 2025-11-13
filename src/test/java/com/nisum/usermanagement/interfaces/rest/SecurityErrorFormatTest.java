package com.nisum.usermanagement.interfaces.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityErrorFormatTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn401WithStandardErrorFormat() throws Exception {
        UUID userId = UUID.randomUUID();
        
        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Autenticación requerida. Token JWT inválido o ausente."));
    }

    @Test
    void shouldReturn401WithInvalidTokenFormat() throws Exception {
        UUID userId = UUID.randomUUID();
        
        mockMvc.perform(get("/api/v1/users/" + userId)
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Autenticación requerida. Token JWT inválido o ausente."));
    }
}
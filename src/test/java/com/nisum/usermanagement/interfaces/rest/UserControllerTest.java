package com.nisum.usermanagement.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.usermanagement.application.dto.PhoneRequest;
import com.nisum.usermanagement.application.dto.UserRequest;
import com.nisum.usermanagement.application.dto.UserResponse;
import com.nisum.usermanagement.application.exception.DuplicateEmailException;
import com.nisum.usermanagement.application.service.GetUserService;
import com.nisum.usermanagement.application.service.RegisterUserService;
import com.nisum.usermanagement.infrastructure.security.JwtService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegisterUserService registerUserService;

    @MockitoBean
    private GetUserService getUserService;
    
    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        UserRequest request = createUserRequest("Juan Rodriguez", "juan@rodriguez.org", "Password123!", "1234567");
        UserResponse response = createUserResponse();

        when(registerUserService.register(any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Juan Rodriguez"))
                .andExpect(jsonPath("$.email").value("juan@rodriguez.org"))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.isactive").value(true));
    }

    @Test
    void shouldReturn409WhenEmailDuplicated() throws Exception {
        UserRequest request = createUserRequest("Juan Rodriguez", "juan@rodriguez.org", "Password123!", "1234567");

        when(registerUserService.register(any(UserRequest.class)))
                .thenThrow(new DuplicateEmailException("El correo ya se encuentra registrado"));

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El correo ya se encuentra registrado"));
    }

    @Test
    void shouldReturn400WhenEmailFormatInvalid() throws Exception {
        UserRequest request = createUserRequest("Juan Rodriguez", "invalid-email", "Password123!", "1234567");

        when(registerUserService.register(any(UserRequest.class)))
                .thenThrow(new IllegalArgumentException("Formato de correo inválido"));

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Formato de correo inválido"));
    }

    @Test
    void shouldReturn400WhenPasswordInvalid() throws Exception {
        UserRequest request = createUserRequest("Juan Rodriguez", "juan@rodriguez.org", "weakpassword", "1234567");

        when(registerUserService.register(any(UserRequest.class)))
                .thenThrow(new IllegalArgumentException("Formato de contraseña inválido"));

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Formato de contraseña inválido"));
    }

    @Test
    void shouldReturn400WhenRequestBodyInvalid() throws Exception {
        UserRequest request = new UserRequest("", "", "", null);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    private UserRequest createUserRequest(String name, String email, String password, String phoneNumber) {
        List<PhoneRequest> phones = new ArrayList<>();
        phones.add(new PhoneRequest(phoneNumber, "1", "57"));
        return new UserRequest(name, email, password, phones);
    }

    private UserResponse createUserResponse() {
        return new UserResponse(
            UUID.randomUUID(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            "jwt-token",
            true,
            "Juan Rodriguez",
            "juan@rodriguez.org",
            new ArrayList<>()
        );
    }
}

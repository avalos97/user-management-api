package com.nisum.usermanagement.interfaces.rest;

import com.nisum.usermanagement.application.dto.UserRequest;
import com.nisum.usermanagement.application.dto.UserResponse;
import com.nisum.usermanagement.application.service.GetUserService;
import com.nisum.usermanagement.application.service.RegisterUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final RegisterUserService registerUserService;
    private final GetUserService getUserService;

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        UserResponse response = registerUserService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        UserResponse response = getUserService.getUserById(id);
        return ResponseEntity.ok(response);
    }
}

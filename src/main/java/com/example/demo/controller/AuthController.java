package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(authService.registerUser(user));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.loginUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
    
    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
    
    @Data
    @RequiredArgsConstructor
    public static class AuthResponse {
        private final String token;
    }
}
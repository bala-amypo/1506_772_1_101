package com.example.demo.controller;

import com.example.demo.config.JwtProvider;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(com.example.demo.model.Role.ROLE_USER));
        
        User savedUser = userRepository.save(user);
        
        String token = jwtProvider.generateToken(
            savedUser.getEmail(), 
            savedUser.getId(), 
            Set.of("ROLE_USER"));
        
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "User registered successfully");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        
        String token = jwtProvider.generateToken(
            user.getEmail(), 
            user.getId(), 
            Set.of("ROLE_USER"));
        
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Login successful");
        
        return ResponseEntity.ok(response);
    }
}
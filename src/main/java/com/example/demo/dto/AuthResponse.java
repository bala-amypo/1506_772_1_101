package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private String email;
    private Long userId;
    
    // For backward compatibility
    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }
}
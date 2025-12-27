package com.example.demo.config;

import com.example.demo.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long validityInMilliseconds;
    
    // Updated to accept roles parameter
    public String generateToken(String username, Long userId, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", userId);
        
        // Extract role names
        List<String> roleNames = roles.stream()
                .map(Enum::name) // Assuming Role is an enum
                .collect(Collectors.toList());
        claims.put("roles", roleNames);
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
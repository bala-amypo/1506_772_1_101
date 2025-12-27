package com.example.demo.config;

import com.example.demo.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    
    @Value("${jwt.secret:defaultSecretKeyForDevelopmentUseOnlyChangeInProduction}")
    private String secret;
    
    @Value("${jwt.expiration:3600000}") // 1 hour default
    private long validityInMilliseconds;
    
    // Method to generate token with roles
    public String generateToken(String username, Long userId, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", userId);
        
        // Convert roles to list of role names
        List<String> roleNames = roles.stream()
                .map(Role::name)
                .collect(Collectors.toList());
        claims.put("roles", roleNames);
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    
    // Method to validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Token validation failed
            return false;
        }
    }
    
    // Method to get email/username from token
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    // Method to get user ID from token
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }
    
    // Method to get roles from token
    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        
        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);
        
        if (roles == null) {
            return Collections.emptyList();
        }
        
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    
    // Method to get Authentication object from token
    public Authentication getAuthentication(String token) {
        String email = getEmailFromToken(token);
        List<SimpleGrantedAuthority> authorities = getRolesFromToken(token);
        
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
    
    // ADD THIS METHOD TO FIX THE TEST ERROR
    public Long getUserId(String token) {
        return getUserIdFromToken(token);
    }
}
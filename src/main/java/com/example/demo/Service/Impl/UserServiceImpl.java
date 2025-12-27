// package com.example.demo.service.impl;

// import com.example.demo.config.JwtProvider;
// import com.example.demo.dto.AuthRequest;
// import com.example.demo.dto.AuthResponse;
// import com.example.demo.dto.UserRegisterDto;
// import com.example.demo.exception.ResourceNotFoundException;
// import com.example.demo.model.Role;
// import com.example.demo.model.User;
// import com.example.demo.repository.UserRepository;
// import com.example.demo.service.UserService;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.Set;
// import java.util.stream.Collectors;

// @Service
// public class UserServiceImpl implements UserService {

//     private final UserRepository userRepository;
//     private final PasswordEncoder passwordEncoder;
//     private final JwtProvider jwtProvider;

//     public UserServiceImpl(UserRepository userRepository,
//                            PasswordEncoder passwordEncoder,
//                            JwtProvider jwtProvider) {
//         this.userRepository = userRepository;
//         this.passwordEncoder = passwordEncoder;
//         this.jwtProvider = jwtProvider;
//     }

//     @Override
//     public User register(UserRegisterDto dto) {

//         userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
//             throw new IllegalArgumentException("Email already exists");
//         });

//         Set<Role> roles = dto.getRoles() == null
//                 ? Set.of(Role.ROLE_USER)
//                 : dto.getRoles().stream()
//                         .map(r -> Role.valueOf("ROLE_" + r))
//                         .collect(Collectors.toSet());

//         User user = User.builder()
//                 .name(dto.getName())
//                 .email(dto.getEmail())
//                 .password(passwordEncoder.encode(dto.getPassword()))
//                 .roles(roles)
//                 .createdAt(LocalDateTime.now())
//                 .build();

//         return userRepository.save(user);
//     }

//     @Override
//     public AuthResponse login(AuthRequest request) {

//         User user = userRepository.findByEmail(request.getEmail())
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

//         String token = jwtProvider.generateToken(
//                 user.getEmail(),
//                 user.getId(),
//                 user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
//         );

//         return new AuthResponse(
//                 token,
//                 user.getId(),
//                 user.getEmail(),
//                 user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
//         );
//     }

//     @Override
//     public User getByEmail(String email) {
//         return userRepository.findByEmail(email)
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//     }
// }
package com.example.demo.service.impl;

import com.example.demo.config.JwtProvider;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.UserRegisterDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User register(UserRegisterDto dto) {

        // Check if email already exists
        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email already exists");
        });

        // Convert roles from DTO safely
        Set<Role> roles;
        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            roles = Set.of(Role.ROLE_USER); // default role
        } else {
            roles = dto.getRoles().stream()
                    .map(r -> {
                        String roleStr = r.toUpperCase();
                        // Allow both "USER"/"ADMIN" and "ROLE_USER"/"ROLE_ADMIN"
                        if (!roleStr.startsWith("ROLE_")) {
                            roleStr = "ROLE_" + roleStr;
                        }
                        return Role.valueOf(roleStr);
                    })
                    .collect(Collectors.toSet());
        }

        // Build user entity
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(roles)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @Override
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtProvider.generateToken(
                user.getEmail(),
                user.getId(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

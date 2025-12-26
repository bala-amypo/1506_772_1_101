package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {

        // 1️⃣ Name validation
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Name must not be empty");
        }

        // 2️⃣ Password validation
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }

        // 3️⃣ Email format validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (user.getEmail() == null ||
                !Pattern.matches(emailRegex, user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // 4️⃣ Email uniqueness check
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // 5️⃣ Default role assignment
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.ROLE_USER));
        }

        // 6️⃣ CreatedAt timestamp
        user.setCreatedAt(LocalDateTime.now());

        // 7️⃣ Save user
        return userRepository.save(user);
    }
}

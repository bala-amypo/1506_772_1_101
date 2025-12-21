package com.example.demo.service;

import com.example.demo.model.User;
import java.util.Map;

public interface UserService {
    User register(User user);
    Map<String, Object> login(String email, String password);
    User getByEmail(String email);
}
package com.example.demo1.service;

import com.example.demo1.dto.AuthRequest;
import com.example.demo1.dto.AuthResponse;
import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(AuthRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "User with this email already exists");
        }

        // Create new user
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            userRepository.save(user);
            return new AuthResponse(true, "User registered successfully");
        } catch (Exception e) {
            return new AuthResponse(false, "Failed to register user: " + e.getMessage());
        }
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return new AuthResponse(false, "User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(false, "Invalid password");
        }

        // For now, return success. You can add JWT token generation here later
        return new AuthResponse(true, "Login successful");
    }
}
package com.example.demo1.controller;

import com.example.demo1.dto.AuthRequest;
import com.example.demo1.dto.AuthResponse;
import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        System.out.println("Received registration: " + request.getEmail() + ", " + request.getFullName());
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        if (response.isSuccess()) {
            // Get user details from the repository to ensure we have the full name
            userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    System.out.println("User logged in: " + user.getEmail() + ", " + user.getFullName());
                });
        } else {
            System.out.println("Login attempt failed for email: " + request.getEmail());
        }
        return ResponseEntity.ok(response);
    }
}

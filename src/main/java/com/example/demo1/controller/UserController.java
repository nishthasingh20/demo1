package com.example.demo1.controller;

import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        if (userRepository.existsById(user.getEmail())) {
            return ResponseEntity.badRequest().body("User with email already exists");
        }

        userRepository.save(user);
        return ResponseEntity.ok("User created successfully");
    }
}

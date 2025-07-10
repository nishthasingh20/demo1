package com.example.demo1.controller;

import com.example.demo1.dto.UserDTO;
import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getFullName(), user.getEmail()))
                .toList();
        return ResponseEntity.ok(users);
    }

}

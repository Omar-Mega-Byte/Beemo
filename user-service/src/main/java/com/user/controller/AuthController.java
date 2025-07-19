package com.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.exceptions.BadRequestException;
import com.user.exceptions.NotFoundException;
import com.user.model.User;
import com.user.service.UserService;
import com.user.util.JwtUtil;
import com.user.validator.UserValidator;

@RestController
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Validate user input
        UserValidator.validateUser(user);
        
        // Check if username already exists
        if (userService.findByUsername(user.getUsername()) != null) {
            throw new BadRequestException("Username already exists");
        }
        
        // Check if email already exists
        if (userService.findByEmail(user.getEmail()) != null) {
            throw new BadRequestException("Email already exists");
        }
        
        // Register the user
        User savedUser = userService.registerUser(user);
        
        // Return success response without sensitive data
        return ResponseEntity.ok(Map.of(
            "message", "User registered successfully",
            "userId", savedUser.getId(),
            "username", savedUser.getUsername()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        // Validate login credentials
        UserValidator.validateLoginCredentials(username, password);
        
        // Find user by username
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        
        // Check password
        if (!userService.checkPassword(password, user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(username);
        
        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "token", token,
            "userId", user.getId(),
            "username", user.getUsername(),
            "role", user.getRole()
        ));
    }
}
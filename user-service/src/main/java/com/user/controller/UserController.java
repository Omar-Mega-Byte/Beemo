package com.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.user.model.User;
import com.user.service.UserService;

@RestController
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user != null);
    }
}

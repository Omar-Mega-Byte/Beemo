package com.trader.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.trader.exceptions.BadRequestException;
import com.trader.exceptions.NotFoundException;
import com.trader.model.Trader;
import com.trader.service.TraderService;
import com.trader.util.JwtUtil;
import com.trader.validator.TraderValidator;

/**
 * Authentication Controller for Trader Service
 * 
 * This controller handles trader registration and login operations.
 * It provides endpoints for trader authentication and uses proper
 * validation and exception handling.
 */
@RestController
public class AuthController {
    private final TraderService traderService;
    private final JwtUtil jwtUtil;
    
    /**
     * Constructor for dependency injection
     * 
     * @param traderService Service for trader operations
     * @param jwtUtil Utility for JWT token operations
     */
    public AuthController(TraderService traderService, JwtUtil jwtUtil) {
        this.traderService = traderService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new trader
     * 
     * @param trader The trader data to register
     * @return ResponseEntity with registration result
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Trader trader) {
        // Validate trader input
        TraderValidator.validateTrader(trader);
        
        // Check if username already exists
        if (traderService.findByUsername(trader.getUsername()) != null) {
            throw new BadRequestException("Username already exists");
        }
        
        // Check if email already exists
        if (traderService.findByEmail(trader.getEmail()) != null) {
            throw new BadRequestException("Email already exists");
        }
        
        // Register the trader
        Trader savedTrader = traderService.registerTrader(trader);
        
        // Return success response without sensitive data
        return ResponseEntity.ok(Map.of(
            "message", "Trader registered successfully",
            "traderId", savedTrader.getId(),
            "username", savedTrader.getUsername(),
            "company", savedTrader.getCompany()
        ));
    }

    /**
     * Login a trader
     * 
     * @param loginData Map containing username and password
     * @return ResponseEntity with login result and JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        // Validate login credentials
        TraderValidator.validateLoginCredentials(username, password);
        
        // Find trader by username
        Trader trader = traderService.findByUsername(username);
        if (trader == null) {
            throw new NotFoundException("Trader not found");
        }
        
        // Check password
        if (!traderService.checkPassword(password, trader.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(username);
        
        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "token", token,
            "traderId", trader.getId(),
            "username", trader.getUsername(),
            "role", trader.getRole(),
            "company", trader.getCompany()
        ));
    }
}

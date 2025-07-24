package com.trader.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.trader.model.Trader;
import com.trader.repository.TraderRepository;

/**
 * Service class for Trader operations
 * 
 * This service handles business logic for trader registration,
 * authentication, and data management. It provides methods for
 * trader CRUD operations and password management.
 */
@Service
public class TraderService {
    private final TraderRepository traderRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Constructor for dependency injection
     * 
     * @param traderRepository Repository for trader data operations
     * @param passwordEncoder Encoder for password hashing
     */
    public TraderService(TraderRepository traderRepository, BCryptPasswordEncoder passwordEncoder) {
        this.traderRepository = traderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new trader with encrypted password
     * 
     * @param trader The trader to register
     * @return The saved trader entity
     */
    public Trader registerTrader(Trader trader) {
        trader.setPassword(passwordEncoder.encode(trader.getPassword()));
        trader.setRole("TRADER");
        return traderRepository.save(trader);
    }

    /**
     * Find a trader by ID
     * 
     * @param id The trader ID
     * @return The trader if found, null otherwise
     */
    public Trader findTraderById(Long id) {
        return traderRepository.findById(id).orElse(null);
    }

    /**
     * Find a trader by username
     * 
     * @param username The username to search for
     * @return The trader if found, null otherwise
     */
    public Trader findByUsername(String username) {
        return traderRepository.findByUsername(username).orElse(null);
    }

    /**
     * Find a trader by email
     * 
     * @param email The email to search for
     * @return The trader if found, null otherwise
     */
    public Trader findByEmail(String email) {
        return traderRepository.findByEmail(email).orElse(null);
    }

    /**
     * Check if a raw password matches the encoded password
     * 
     * @param rawPassword The plain text password
     * @param encodedPassword The encoded password from database
     * @return true if passwords match, false otherwise
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Delete a trader by ID
     * 
     * @param id The trader ID to delete
     */
    public void deleteTrader(Long id) {
        traderRepository.deleteById(id);
    }
}

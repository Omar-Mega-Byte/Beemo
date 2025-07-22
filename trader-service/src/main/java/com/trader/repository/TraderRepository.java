package com.trader.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trader.model.Trader;

/**
 * Repository interface for Trader entity operations
 * 
 * This interface extends JpaRepository to provide CRUD operations
 * for Trader entities and includes custom query methods for
 * authentication and validation purposes.
 */
public interface TraderRepository extends JpaRepository<Trader, Long> {
    
    /**
     * Find a trader by username
     * 
     * @param username The username to search for
     * @return Optional containing the trader if found, empty otherwise
     */
    Optional<Trader> findByUsername(String username);
    
    /**
     * Find a trader by email address
     * 
     * @param email The email address to search for
     * @return Optional containing the trader if found, empty otherwise
     */
    Optional<Trader> findByEmail(String email);
}

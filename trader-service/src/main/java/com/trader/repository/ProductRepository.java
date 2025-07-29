package com.trader.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trader.model.Product;

/**
 * Repository interface for Product entity operations
 * 
 * This interface extends JpaRepository to provide CRUD operations
 * for Product entities and includes custom query methods for
 * product retrieval.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
	/**
	 * Save a product entity
	 * 
	 * @param product The product entity to save
	 * @return The saved product entity
	 */
	/**
	 * Find a product by its name
	 * 
	 * @param name The name of the product to search for
	 * @return Optional containing the product if found, empty otherwise
	 */
	Optional<Product> findByName(String name);
}

package com.trader.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trader.model.Product;
import com.trader.repository.ProductRepository;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Save a product entity
	 * 
	 * @param product The product entity to save
	 * @return The saved product entity
	 */
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	/**
	 * Find a product by its name
	 * 
	 * @param name The name of the product to search for
	 * @return The product if found, null otherwise
	 */
	public Product findByName(String name) {
		return productRepository.findByName(name).orElse(null);
	}

	/**
	 * Retrieve all products
	 * 
	 * @return List of all products
	 */
	public List<Product> findAllProducts() {
		return productRepository.findAll();
	}

	/**
	 * Find a product by its ID
	 * 
	 * @param id The ID of the product to search for
	 * @return The product if found, null otherwise
	 */
	public Product findById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	public void deleteProduct(Long id) {
		if (productRepository.existsById(id)) {
			productRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("Product with ID " + id + " does not exist.");
		}
	}
}
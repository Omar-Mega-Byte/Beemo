package com.trader.controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trader.model.Product;
import com.trader.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> products = productService.findAllProducts();
		return ResponseEntity.ok(products);
	}

	@PostMapping
	public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
		try {
			Product savedProduct = productService.saveProduct(product);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Product with name '" + product.getName() + "' already exists");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Failed to create product: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Product product = productService.findById(id);
		return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		try {
			Product existingProduct = productService.findById(id);
			if (existingProduct == null) {
				return ResponseEntity.notFound().build();
			}
			productService.deleteProduct(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
		try {
			Product existingProduct = productService.findById(id);
			if (existingProduct == null) {
				return ResponseEntity.notFound().build();
			}
			product.setId(id);
			Product updatedProduct = productService.saveProduct(product);
			return ResponseEntity.ok(updatedProduct);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Product with name '" + product.getName() + "' already exists");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Failed to update product: " + e.getMessage());
		}
	}

	@GetMapping("/{id}/stock")
	public ResponseEntity<?> checkStock(@PathVariable Long id, @RequestParam int quantity) {
		try {
			boolean hasStock = productService.hasInStock(id, quantity);
			return ResponseEntity.ok(hasStock);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Error checking stock: " + e.getMessage());
		}
	}

	@PutMapping("/{id}/stock")
	public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestParam int quantity) {
		try {
			boolean updated = productService.updateProductStock(id, quantity);
			if (updated) {
				return ResponseEntity.ok("Stock updated successfully");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Insufficient stock or product not found");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Error updating stock: " + e.getMessage());
		}
	}
}

package com.trader.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.trader.model.Product;
import com.trader.service.ProductService;
@RestController
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/products")
	public List<Product> getAllProducts() {
		return productService.findAllProducts();
	}

	@PostMapping("/products")
	public Product createProduct(@RequestBody Product product) {
		return productService.saveProduct(product);
	}
}

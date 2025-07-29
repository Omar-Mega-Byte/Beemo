package com.order.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.order.dto.ProductDto;

@Component
public class TraderServiceClient {
    
    private final RestTemplate restTemplate;
    private final String traderServiceUrl = "http://localhost:9006";
    
    public TraderServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Fetch product details from trader-service
     * 
     * @param productId The ID of the product to fetch
     * @return Product details or null if not found
     */
    public ProductDto getProduct(Long productId) {
        try {
            String url = traderServiceUrl + "/products/" + productId;
            return restTemplate.getForObject(url, ProductDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with trader-service: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if product has sufficient stock
     * 
     * @param productId The ID of the product
     * @param quantity The requested quantity
     * @return true if sufficient stock available
     */
    public boolean checkStock(Long productId, int quantity) {
        try {
            String url = traderServiceUrl + "/products/" + productId + "/stock?quantity=" + quantity;
            Boolean hasStock = restTemplate.getForObject(url, Boolean.class);
            return hasStock != null && hasStock;
        } catch (Exception e) {
            throw new RuntimeException("Error checking stock with trader-service: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update product stock after successful order
     * 
     * @param productId The ID of the product
     * @param quantity The quantity to reduce
     * @return true if stock update successful
     */
    public boolean updateStock(Long productId, int quantity) {
        try {
            String url = traderServiceUrl + "/products/" + productId + "/stock?quantity=" + quantity;
            restTemplate.put(url, null);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error updating stock with trader-service: " + e.getMessage(), e);
        }
    }
}

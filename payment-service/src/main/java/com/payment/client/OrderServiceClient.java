package com.payment.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import com.payment.dto.OrderDto;

@Component
public class OrderServiceClient {
    
    private final RestTemplate restTemplate;
    private final String orderServiceUrl = "http://localhost:9001";
    
    public OrderServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Fetch order details from order-service
     */
    public OrderDto getOrder(Long orderId) {
        try {
            String url = orderServiceUrl + "/orders/" + orderId;
            return restTemplate.getForObject(url, OrderDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with order-service: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get order by ID (alias for getOrder)
     */
    public OrderDto getOrderById(Long orderId) {
        return getOrder(orderId);
    }
    
    /**
     * Update order status after payment
     */
    public boolean updateOrderStatus(Long orderId, String status) {
        try {
            String url = orderServiceUrl + "/orders/" + orderId + "/status?status=" + status;
            restTemplate.put(url, null);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error updating order status: " + e.getMessage(), e);
        }
    }
}

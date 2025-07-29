package com.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find all orders for a specific user
     * 
     * @param userId The ID of the user
     * @return List of orders for the user
     */
    List<Order> findByUserId(Long userId);
    
    /**
     * Find all orders for a specific product
     * 
     * @param productId The ID of the product
     * @return List of orders for the product
     */
    List<Order> findByProductId(Long productId);
    
    /**
     * Find orders by status
     * 
     * @param status The order status
     * @return List of orders with the given status
     */
    List<Order> findByStatus(String status);
}

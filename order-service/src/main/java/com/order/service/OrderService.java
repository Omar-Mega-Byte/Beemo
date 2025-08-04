package com.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.client.TraderServiceClient;
import com.order.client.UserServiceClient;
import com.order.dto.OrderRequest;
import com.order.dto.ProductDto;
import com.order.exceptions.InsufficientStockException;
import com.order.exceptions.ProductNotFoundException;
import com.order.exceptions.UserNotFoundException;
import com.order.model.Order;
import com.order.repository.OrderRepository;

@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;
    private final TraderServiceClient traderServiceClient;
    
    public OrderService(OrderRepository orderRepository, 
                        UserServiceClient userServiceClient, 
                        TraderServiceClient traderServiceClient) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
        this.traderServiceClient = traderServiceClient;
    }
    
    /**
     * Create a new order with comprehensive validation
     * 
     * @param orderRequest The order request containing user ID, product ID, and quantity
     * @return The created order
     * @throws UserNotFoundException if user doesn't exist
     * @throws ProductNotFoundException if product doesn't exist
     * @throws InsufficientStockException if not enough stock available
     */
    public Order createOrder(OrderRequest orderRequest) {
        // Step 1: Validate user exists
        if (!userServiceClient.validateUser(orderRequest.getUserId())) {
            throw new UserNotFoundException("User with ID " + orderRequest.getUserId() + " not found");
        }
        
        // Step 2: Fetch product details
        ProductDto product = traderServiceClient.getProduct(orderRequest.getProductId());
        if (product == null) {
            throw new ProductNotFoundException("Product with ID " + orderRequest.getProductId() + " not found");
        }
        
        // Step 3: Check stock availability
        if (!traderServiceClient.checkStock(orderRequest.getProductId(), orderRequest.getQuantity())) {
            throw new InsufficientStockException("Insufficient stock for product " + product.getName() + 
                ". Requested: " + orderRequest.getQuantity() + ", Available: " + product.getStock());
        }
        
        // Step 4: Calculate total price
        double totalPrice = product.getPrice() * orderRequest.getQuantity();
        
        // Step 5: Create order
        Order order = new Order(
            orderRequest.getUserId(),
            orderRequest.getProductId(),
            orderRequest.getQuantity(),
            totalPrice
        );
        
        // Step 6: Save order
        Order savedOrder = orderRepository.save(order);
        
        // Step 7: Update product stock
        boolean stockUpdated = traderServiceClient.updateStock(
            orderRequest.getProductId(), 
            orderRequest.getQuantity()
        );
        
        if (!stockUpdated) {
            throw new RuntimeException("Failed to update product stock");
        }
        
        // Step 8: Update order status to confirmed
        savedOrder.setStatus("CONFIRMED");
        return orderRepository.save(savedOrder);
    }
    
    /**
     * Get all orders for a specific user
     * 
     * @param userId The ID of the user
     * @return List of orders for the user
     */
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
    /**
     * Get order by ID
     * 
     * @param orderId The ID of the order
     * @return The order if found, null otherwise
     */
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
    
    /**
     * Get all orders
     * 
     * @return List of all orders
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    /**
     * Cancel an order (only if status is PENDING or CONFIRMED)
     * 
     * @param orderId The ID of the order to cancel
     * @return The cancelled order
     */
    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
        
        if ("CANCELLED".equals(order.getStatus()) || "DELIVERED".equals(order.getStatus())) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }
        
        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }
    
    /**
     * Update order status
     * 
     * @param orderId The ID of the order
     * @param status The new status to set
     * @return The updated order
     */
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
        
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    /**
     * Get order status by ID
     * 
     * @param orderId The ID of the order
     * @return The order status
     */
    public String getOrderStatus(Long orderId) {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
        return order.getStatus();
    }
}

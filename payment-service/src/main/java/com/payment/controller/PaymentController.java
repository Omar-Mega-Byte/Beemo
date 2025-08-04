package com.payment.controller;

import com.payment.dto.PaymentRequest;
import com.payment.model.Payment;
import com.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private PaymentService paymentService;
    
    /**
     * Process a new payment
     */
    @PostMapping
    public ResponseEntity<Payment> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        logger.info("Received payment request for order: {}", paymentRequest.getOrderId());
        
        Payment payment = paymentService.processPayment(paymentRequest);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }
    
    /**
     * Get payment by ID
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId) {
        logger.info("Fetching payment with ID: {}", paymentId);
        
        Payment payment = paymentService.getPaymentById(paymentId);
        
        return ResponseEntity.ok(payment);
    }
    
    /**
     * Get payment by order ID
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable Long orderId) {
        logger.info("Fetching payment for order ID: {}", orderId);
        
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        
        return ResponseEntity.ok(payment);
    }
    
    /**
     * Get payment by transaction ID
     */
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getPaymentByTransactionId(@PathVariable String transactionId) {
        logger.info("Fetching payment with transaction ID: {}", transactionId);
        
        Payment payment = paymentService.getPaymentByTransactionId(transactionId);
        
        return ResponseEntity.ok(payment);
    }
    
    /**
     * Get payments by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUserId(@PathVariable Long userId) {
        logger.info("Fetching payments for user ID: {}", userId);
        
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
        
        return ResponseEntity.ok(payments);
    }
    
    /**
     * Get payments by user ID and status
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByUserIdAndStatus(
            @PathVariable Long userId, 
            @PathVariable String status) {
        logger.info("Fetching payments for user ID: {} with status: {}", userId, status);
        
        Payment.PaymentStatus paymentStatus;
        try {
            paymentStatus = Payment.PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Payment> payments = paymentService.getPaymentsByUserIdAndStatus(userId, paymentStatus);
        
        return ResponseEntity.ok(payments);
    }
    
    /**
     * Process a refund
     */
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Payment> processRefund(
            @PathVariable Long paymentId,
            @RequestBody Map<String, Object> refundRequest) {
        logger.info("Processing refund for payment ID: {}", paymentId);
        
        // Extract refund amount and reason from request
        BigDecimal refundAmount;
        String reason = (String) refundRequest.get("reason");
        
        try {
            Object amountObj = refundRequest.get("amount");
            if (amountObj instanceof Number numberAmount) {
                refundAmount = BigDecimal.valueOf(numberAmount.doubleValue());
            } else if (amountObj instanceof String stringAmount) {
                refundAmount = new BigDecimal(stringAmount);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            logger.error("Invalid refund amount format", e);
            return ResponseEntity.badRequest().build();
        }
        
        if (reason == null || reason.trim().isEmpty()) {
            reason = "Refund requested";
        }
        
        Payment refundPayment = paymentService.processRefund(paymentId, refundAmount, reason);
        
        return ResponseEntity.ok(refundPayment);
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "payment-service",
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
    
    /**
     * Get payment status by order ID (simplified endpoint for order service)
     */
    @GetMapping("/order/{orderId}/status")
    public ResponseEntity<Map<String, String>> getPaymentStatusByOrderId(@PathVariable Long orderId) {
        logger.info("Fetching payment status for order ID: {}", orderId);
        
        try {
            Payment payment = paymentService.getPaymentByOrderId(orderId);
            return ResponseEntity.ok(Map.of(
                    "orderId", orderId.toString(),
                    "status", payment.getStatus().name(),
                    "transactionId", payment.getTransactionId() != null ? payment.getTransactionId() : ""
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "orderId", orderId.toString(),
                    "status", "NOT_FOUND"
            ));
        }
    }
}

package com.payment.service;

import com.payment.client.OrderServiceClient;
import com.payment.client.UserServiceClient;
import com.payment.dto.OrderDto;
import com.payment.dto.PaymentRequest;
import com.payment.exceptions.InvalidPaymentMethodException;
import com.payment.exceptions.OrderValidationException;
import com.payment.exceptions.PaymentNotFoundException;
import com.payment.exceptions.PaymentProcessingException;
import com.payment.model.Payment;
import com.payment.repository.PaymentRepository;
import com.payment.service.PaymentGatewayService.PaymentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PaymentGatewayService paymentGatewayService;
    
    @Autowired
    private OrderServiceClient orderServiceClient;
    
    @Autowired
    private UserServiceClient userServiceClient;
    
    /**
     * Process a new payment
     */
    public Payment processPayment(PaymentRequest paymentRequest) {
        logger.info("Processing payment for order: {} with method: {}", 
                   paymentRequest.getOrderId(), paymentRequest.getPaymentMethod());
        
        // Validate order exists and is pending payment
        validateOrder(paymentRequest.getOrderId());
        
        // Validate user exists
        validateUser(paymentRequest.getUserId());
        
        // Check if payment already exists for this order
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(paymentRequest.getOrderId());
        if (existingPayment.isPresent() && 
            (existingPayment.get().getStatus() == Payment.PaymentStatus.COMPLETED ||
             existingPayment.get().getStatus() == Payment.PaymentStatus.PENDING)) {
            throw new PaymentProcessingException("Payment already exists for order: " + paymentRequest.getOrderId());
        }
        
        // Create payment record
        Payment payment = createPaymentRecord(paymentRequest);
        payment = paymentRepository.save(payment);
        
        try {
            // Process payment through gateway
            PaymentResult result = processPaymentThroughGateway(paymentRequest);
            
            if (result.isSuccess()) {
                // Update payment status to completed
                payment.setStatus(Payment.PaymentStatus.COMPLETED);
                payment.setTransactionId(result.getTransactionId());
                payment.setCompletedAt(LocalDateTime.now());
                
                // Update order status to paid
                orderServiceClient.updateOrderStatus(paymentRequest.getOrderId(), "PAID");
                
                logger.info("Payment completed successfully for order: {} with transaction ID: {}", 
                           paymentRequest.getOrderId(), result.getTransactionId());
            } else {
                // Update payment status to failed
                payment.setStatus(Payment.PaymentStatus.FAILED);
                payment.setFailureReason(result.getMessage());
                
                logger.warn("Payment failed for order: {} - {}", 
                           paymentRequest.getOrderId(), result.getMessage());
                
                throw new PaymentProcessingException("Payment processing failed: " + result.getMessage());
            }
            
            return paymentRepository.save(payment);
            
        } catch (Exception e) {
            // Update payment status to failed
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason(e.getMessage());
            paymentRepository.save(payment);
            
            logger.error("Payment processing error for order: {}", paymentRequest.getOrderId(), e);
            
            if (e instanceof PaymentProcessingException) {
                throw e;
            }
            throw new PaymentProcessingException("Unexpected error during payment processing: " + e.getMessage());
        }
    }
    
    /**
     * Get payment by ID
     */
    @Transactional(readOnly = true)
    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));
    }
    
    /**
     * Get payment by order ID
     */
    @Transactional(readOnly = true)
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order ID: " + orderId));
    }
    
    /**
     * Get payments by user ID
     */
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
    
    /**
     * Get payments by user ID and status
     */
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByUserIdAndStatus(Long userId, Payment.PaymentStatus status) {
        return paymentRepository.findByUserIdAndStatus(userId, status);
    }
    
    /**
     * Get payment by transaction ID
     */
    @Transactional(readOnly = true)
    public Payment getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with transaction ID: " + transactionId));
    }
    
    /**
     * Process refund
     */
    public Payment processRefund(Long paymentId, BigDecimal refundAmount, String reason) {
        logger.info("Processing refund for payment ID: {} with amount: {}", paymentId, refundAmount);
        
        Payment originalPayment = getPaymentById(paymentId);
        
        // Validate payment can be refunded
        if (originalPayment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new PaymentProcessingException("Can only refund completed payments");
        }
        
        if (refundAmount.compareTo(originalPayment.getAmount()) > 0) {
            throw new PaymentProcessingException("Refund amount cannot exceed original payment amount");
        }
        
        // Check if full refund already exists
        Optional<Payment> existingRefund = paymentRepository.findByOrderIdAndStatus(
                originalPayment.getOrderId(), Payment.PaymentStatus.REFUNDED);
        if (existingRefund.isPresent()) {
            throw new PaymentProcessingException("Payment has already been refunded");
        }
        
        try {
            // Process refund through gateway
            PaymentResult refundResult = paymentGatewayService.processRefund(
                    originalPayment.getTransactionId(), refundAmount);
            
            if (refundResult.isSuccess()) {
                // Create refund payment record
                Payment refundPayment = new Payment();
                refundPayment.setOrderId(originalPayment.getOrderId());
                refundPayment.setUserId(originalPayment.getUserId());
                refundPayment.setAmount(refundAmount.negate()); // Negative amount for refund
                refundPayment.setCurrency(originalPayment.getCurrency());
                refundPayment.setPaymentMethod(originalPayment.getPaymentMethod());
                refundPayment.setStatus(Payment.PaymentStatus.REFUNDED);
                refundPayment.setTransactionId(refundResult.getTransactionId());
                refundPayment.setFailureReason(reason);
                refundPayment.setCompletedAt(LocalDateTime.now());
                
                refundPayment = paymentRepository.save(refundPayment);
                
                // Update order status if full refund
                if (refundAmount.compareTo(originalPayment.getAmount()) == 0) {
                    orderServiceClient.updateOrderStatus(originalPayment.getOrderId(), "REFUNDED");
                }
                
                logger.info("Refund completed successfully for payment ID: {} with transaction ID: {}", 
                           paymentId, refundResult.getTransactionId());
                
                return refundPayment;
            } else {
                throw new PaymentProcessingException("Refund processing failed: " + refundResult.getMessage());
            }
            
        } catch (Exception e) {
            logger.error("Refund processing error for payment ID: {}", paymentId, e);
            
            if (e instanceof PaymentProcessingException) {
                throw e;
            }
            throw new PaymentProcessingException("Unexpected error during refund processing: " + e.getMessage());
        }
    }
    
    /**
     * Validate order exists and is in correct status
     */
    private void validateOrder(Long orderId) {
        try {
            OrderDto order = orderServiceClient.getOrderById(orderId);
            if (order == null) {
                throw new OrderValidationException("Order not found with ID: " + orderId);
            }
            
            if (!"PENDING_PAYMENT".equals(order.getStatus()) && !"CONFIRMED".equals(order.getStatus())) {
                throw new OrderValidationException("Order is not in a state that allows payment. Current status: " + order.getStatus());
            }
            
        } catch (Exception e) {
            if (e instanceof OrderValidationException) {
                throw e;
            }
            throw new OrderValidationException("Failed to validate order: " + e.getMessage());
        }
    }
    
    /**
     * Validate user exists
     */
    private void validateUser(Long userId) {
        try {
            boolean userExists = userServiceClient.validateUser(userId);
            if (!userExists) {
                throw new OrderValidationException("User not found with ID: " + userId);
            }
        } catch (Exception e) {
            if (e instanceof OrderValidationException) {
                throw e;
            }
            throw new OrderValidationException("Failed to validate user: " + e.getMessage());
        }
    }
    
    /**
     * Create payment record from request
     */
    private Payment createPaymentRecord(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        payment.setOrderId(paymentRequest.getOrderId());
        payment.setUserId(paymentRequest.getUserId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentRequest.getPaymentMethod()));
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        
        return payment;
    }
    
    /**
     * Process payment through appropriate gateway
     */
    private PaymentResult processPaymentThroughGateway(PaymentRequest paymentRequest) {
        Payment.PaymentMethod method = Payment.PaymentMethod.valueOf(paymentRequest.getPaymentMethod());
        
        switch (method) {
            case CREDIT_CARD:
                return paymentGatewayService.processCreditCardPayment(
                        paymentRequest.getCardNumber(),
                        paymentRequest.getExpiryDate(),
                        paymentRequest.getCardholderName(),
                        paymentRequest.getCvv(),
                        paymentRequest.getAmount(),
                        paymentRequest.getCurrency()
                );
                
            case DEBIT_CARD:
                return paymentGatewayService.processDebitCardPayment(
                        paymentRequest.getCardNumber(),
                        paymentRequest.getPin(),
                        paymentRequest.getAmount(),
                        paymentRequest.getCurrency()
                );
                
            case PAYPAL:
                return paymentGatewayService.processPayPalPayment(
                        paymentRequest.getPaypalEmail(),
                        paymentRequest.getAmount(),
                        paymentRequest.getCurrency()
                );
                
            case BANK_TRANSFER:
                return paymentGatewayService.processBankTransferPayment(
                        paymentRequest.getAccountNumber(),
                        paymentRequest.getRoutingNumber(),
                        paymentRequest.getAmount(),
                        paymentRequest.getCurrency()
                );
                
            default:
                throw new InvalidPaymentMethodException("Unsupported payment method: " + paymentRequest.getPaymentMethod());
        }
    }
}

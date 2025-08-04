package com.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.payment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * Find payment by order ID
     */
    Optional<Payment> findByOrderId(Long orderId);
    
    /**
     * Find all payments for a specific user
     */
    List<Payment> findByUserId(Long userId);
    
    /**
     * Find payments by status
     */
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    /**
     * Find payment by transaction ID
     */
    Optional<Payment> findByTransactionId(String transactionId);
    
    /**
     * Find payments by user ID and status
     */
    List<Payment> findByUserIdAndStatus(Long userId, Payment.PaymentStatus status);
    
    /**
     * Find payments by order ID and status
     */
    Optional<Payment> findByOrderIdAndStatus(Long orderId, Payment.PaymentStatus status);
    
    /**
     * Find payments by payment method
     */
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    /**
     * Count payments by status
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    long countByStatus(@Param("status") Payment.PaymentStatus status);
    
    /**
     * Find payments by user ID ordered by creation date
     */
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
}

package com.payment.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Random;

@Service
public class PaymentGatewayService {
    
    private final Random random = new Random();
    
    /**
     * Simulate credit card payment processing
     */
    public PaymentResult processCreditCardPayment(String cardNumber, String expiryDate, 
                                                String cardholderName, String cvv, 
                                                BigDecimal amount, String currency) {
        // Simulate payment processing delay
        simulateProcessingDelay();
        
        // Basic validation
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return new PaymentResult(false, "Invalid card number", null);
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(false, "Invalid amount", null);
        }
        
        // Simulate random success/failure (90% success rate)
        boolean success = random.nextInt(100) < 90;
        
        if (success) {
            String transactionId = "CC_" + System.currentTimeMillis() + "_" + random.nextInt(10000);
            return new PaymentResult(true, "Payment processed successfully", transactionId);
        } else {
            return new PaymentResult(false, "Payment declined by bank", null);
        }
    }
    
    /**
     * Simulate debit card payment processing
     */
    public PaymentResult processDebitCardPayment(String cardNumber, String pin, 
                                               BigDecimal amount, String currency) {
        simulateProcessingDelay();
        
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return new PaymentResult(false, "Invalid card number", null);
        }
        
        if (pin == null || pin.length() != 4) {
            return new PaymentResult(false, "Invalid PIN", null);
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(false, "Invalid amount", null);
        }
        
        // Simulate random success/failure (85% success rate)
        boolean success = random.nextInt(100) < 85;
        
        if (success) {
            String transactionId = "DC_" + System.currentTimeMillis() + "_" + random.nextInt(10000);
            return new PaymentResult(true, "Payment processed successfully", transactionId);
        } else {
            return new PaymentResult(false, "Insufficient funds or payment declined", null);
        }
    }
    
    /**
     * Simulate PayPal payment processing
     */
    public PaymentResult processPayPalPayment(String paypalEmail, BigDecimal amount, String currency) {
        simulateProcessingDelay();
        
        if (paypalEmail == null || !paypalEmail.contains("@")) {
            return new PaymentResult(false, "Invalid PayPal email", null);
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(false, "Invalid amount", null);
        }
        
        // Simulate random success/failure (95% success rate)
        boolean success = random.nextInt(100) < 95;
        
        if (success) {
            String transactionId = "PP_" + System.currentTimeMillis() + "_" + random.nextInt(10000);
            return new PaymentResult(true, "PayPal payment processed successfully", transactionId);
        } else {
            return new PaymentResult(false, "PayPal payment failed", null);
        }
    }
    
    /**
     * Simulate bank transfer payment processing
     */
    public PaymentResult processBankTransferPayment(String accountNumber, String routingNumber, 
                                                  BigDecimal amount, String currency) {
        simulateProcessingDelay();
        
        if (accountNumber == null || accountNumber.length() < 10) {
            return new PaymentResult(false, "Invalid account number", null);
        }
        
        if (routingNumber == null || routingNumber.length() != 9) {
            return new PaymentResult(false, "Invalid routing number", null);
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(false, "Invalid amount", null);
        }
        
        // Simulate random success/failure (80% success rate for bank transfers)
        boolean success = random.nextInt(100) < 80;
        
        if (success) {
            String transactionId = "BT_" + System.currentTimeMillis() + "_" + random.nextInt(10000);
            return new PaymentResult(true, "Bank transfer initiated successfully", transactionId);
        } else {
            return new PaymentResult(false, "Bank transfer failed", null);
        }
    }
    
    /**
     * Simulate refund processing
     */
    public PaymentResult processRefund(String originalTransactionId, BigDecimal refundAmount) {
        simulateProcessingDelay();
        
        if (originalTransactionId == null || originalTransactionId.isEmpty()) {
            return new PaymentResult(false, "Invalid transaction ID", null);
        }
        
        if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(false, "Invalid refund amount", null);
        }
        
        // Simulate random success/failure (95% success rate for refunds)
        boolean success = random.nextInt(100) < 95;
        
        if (success) {
            String refundTransactionId = "RF_" + System.currentTimeMillis() + "_" + random.nextInt(10000);
            return new PaymentResult(true, "Refund processed successfully", refundTransactionId);
        } else {
            return new PaymentResult(false, "Refund processing failed", null);
        }
    }
    
    /**
     * Simulate processing delay (between 1-3 seconds)
     */
    private void simulateProcessingDelay() {
        try {
            Thread.sleep(1000 + random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Payment result class
     */
    public static class PaymentResult {
        private final boolean success;
        private final String message;
        private final String transactionId;
        
        public PaymentResult(boolean success, String message, String transactionId) {
            this.success = success;
            this.message = message;
            this.transactionId = transactionId;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getTransactionId() {
            return transactionId;
        }
    }
}

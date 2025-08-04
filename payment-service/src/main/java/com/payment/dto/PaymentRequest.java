package com.payment.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class PaymentRequest {
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotBlank(message = "Currency is required")
    private String currency = "USD";
    
    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "CREDIT_CARD|DEBIT_CARD|PAYPAL|BANK_TRANSFER", 
             message = "Payment method must be one of: CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER")
    private String paymentMethod;
    
    private String description;
    
    // Payment method specific fields
    private String cardNumber;
    private String cardholderName;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
    private String pin; // For debit cards
    
    // PayPal specific
    private String paypalEmail;
    
    // Bank transfer specific
    private String accountNumber;
    private String routingNumber;
    private String bankName;
    
    public PaymentRequest() {
    }
    
    public PaymentRequest(Long orderId, Long userId, BigDecimal amount, String paymentMethod) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getCardholderName() {
        return cardholderName;
    }
    
    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }
    
    public String getExpiryMonth() {
        return expiryMonth;
    }
    
    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }
    
    public String getExpiryYear() {
        return expiryYear;
    }
    
    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }
    
    public String getExpiryDate() {
        if (expiryMonth != null && expiryYear != null) {
            return expiryMonth + "/" + expiryYear;
        }
        return null;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    
    public String getPin() {
        return pin;
    }
    
    public void setPin(String pin) {
        this.pin = pin;
    }
    
    public String getPaypalEmail() {
        return paypalEmail;
    }
    
    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getRoutingNumber() {
        return routingNumber;
    }
    
    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }
    
    public String getBankName() {
        return bankName;
    }
    
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}

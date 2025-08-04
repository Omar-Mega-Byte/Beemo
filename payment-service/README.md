# Payment Service

A comprehensive payment processing microservice built with Spring Boot that handles various payment methods and integrates with other services in the e-commerce platform.

## Features

- **Multiple Payment Methods**: Credit Card, Debit Card, PayPal, Bank Transfer
- **Payment Processing**: Simulated payment gateway integration
- **Order Integration**: Validates orders before processing payments
- **User Validation**: Ensures user exists before processing payments
- **Refund Processing**: Supports full and partial refunds
- **Status Tracking**: Real-time payment status updates
- **Transaction Management**: Secure transaction ID generation
- **Error Handling**: Comprehensive error handling with custom exceptions

## Architecture

### Core Components

1. **Payment Model**: Entity with audit fields and status tracking
2. **PaymentRequest DTO**: Input validation for payment requests
3. **PaymentService**: Business logic for payment processing
4. **PaymentController**: REST API endpoints
5. **PaymentGatewayService**: Simulated payment gateway integration
6. **Service Clients**: Communication with Order and User services

### Payment Methods Supported

- **Credit Card**: Card number, expiry, CVV, cardholder name
- **Debit Card**: Card number, PIN
- **PayPal**: Email address
- **Bank Transfer**: Account number, routing number

### Payment Statuses

- `PENDING`: Payment initiated but not processed
- `PROCESSING`: Payment being processed by gateway
- `COMPLETED`: Payment successfully processed
- `FAILED`: Payment processing failed
- `REFUNDED`: Payment has been refunded

## API Endpoints

### Process Payment
```
POST /payments
```

### Get Payment by ID
```
GET /payments/{paymentId}
```

### Get Payment by Order ID
```
GET /payments/order/{orderId}
```

### Get Payment by Transaction ID
```
GET /payments/transaction/{transactionId}
```

### Get Payments by User ID
```
GET /payments/user/{userId}
```

### Get Payments by User ID and Status
```
GET /payments/user/{userId}/status/{status}
```

### Process Refund
```
POST /payments/{paymentId}/refund
```

### Get Payment Status by Order ID
```
GET /payments/order/{orderId}/status
```

### Health Check
```
GET /payments/health
```

## Configuration

### Database Configuration
- **Database**: MySQL (payment_db)
- **Port**: 3306
- **JPA**: Hibernate with auto-update DDL

### Service Configuration
- **Port**: 9004
- **Eureka**: Service discovery enabled
- **Load Balancing**: RestTemplate with @LoadBalanced

### Integration
- **Order Service**: Validates orders and updates status
- **User Service**: Validates user existence
- **Eureka Server**: Service registration and discovery

## Payment Request Example

### Credit Card Payment
```json
{
  "orderId": 1,
  "userId": 1,
  "amount": 99.99,
  "currency": "USD",
  "paymentMethod": "CREDIT_CARD",
  "cardNumber": "4111111111111111",
  "cardholderName": "John Doe",
  "expiryMonth": "12",
  "expiryYear": "2025",
  "cvv": "123",
  "description": "Order payment"
}
```

### PayPal Payment
```json
{
  "orderId": 1,
  "userId": 1,
  "amount": 99.99,
  "currency": "USD",
  "paymentMethod": "PAYPAL",
  "paypalEmail": "user@example.com",
  "description": "Order payment"
}
```

## Error Handling

### Custom Exceptions
- `PaymentNotFoundException`: Payment not found
- `PaymentProcessingException`: Payment processing errors
- `InvalidPaymentMethodException`: Unsupported payment method
- `OrderValidationException`: Order validation failures

### Global Exception Handler
- Standardized error responses
- HTTP status code mapping
- Detailed error messages

## Security Features

- Input validation for all payment fields
- BigDecimal for financial precision
- Transaction ID generation
- Audit fields for tracking
- Failure reason logging

## Running the Service

1. **Start Dependencies**:
   - MySQL database
   - Eureka server

2. **Configure Database**:
   ```sql
   CREATE DATABASE payment_db;
   ```

3. **Run Service**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Verify Service**:
   - Check Eureka dashboard: http://localhost:8761
   - Health check: http://localhost:9004/payments/health

## Integration Testing

The service integrates with:
- **Order Service** (port 9002): Order validation and status updates
- **User Service** (port 9001): User validation
- **Eureka Server** (port 8761): Service discovery

## Future Enhancements

- Real payment gateway integration (Stripe, PayPal, etc.)
- Webhook support for payment status updates
- Payment analytics and reporting
- Fraud detection and prevention
- Multi-currency support with exchange rates
- Scheduled payment processing
- Payment method tokenization for security

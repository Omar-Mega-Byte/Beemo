
# 🐝 Beemo — E-commerce Microservices System

Beemo is a modular e-commerce application built using **Spring Boot**, following a microservices architecture. It includes core services for users, orders, traders, products, and payments, orchestrated through **Eureka Discovery** and exposed via an **API Gateway**.

---

## 🧱 Project Structure

| Folder             | Description                                        |
|--------------------|----------------------------------------------------|
| `api-gateway`      | API Gateway using Spring Cloud Gateway             |
| `eureka-server`    | Service registry using Netflix Eureka              |
| `user-service`     | Handles registration, login, and user validation   |
| `trader-service`   | Manages traders and product-related actions        |
| `order-service`    | Manages customer orders                            |
| `payment-service`  | Handles payment logic                              |
| `common-models`    | Shared DTOs/entities across services               |
| `postman`          | Collection for testing APIs                        |

---

## 🚀 How to Run

Make sure you have Java 17+ and Maven installed.

1. **Start Eureka Server**
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```

2. **Start API Gateway**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

3. **Start Microservices**
   ```bash
   cd user-service && mvn spring-boot:run
   cd trader-service && mvn spring-boot:run
   cd order-service && mvn spring-boot:run
   cd payment-service && mvn spring-boot:run
   ```

4. **Import Postman Collection**
   * Open the `postman/` folder and import the collection into Postman.
---
# Beemo API Documentation

This document provides an overview of the **Beemo** application's APIs, including TraderService, UserService, OrderService, and PaymentService. All endpoints use JSON for request and response bodies where applicable.

## Base URLs
- **TraderURL**: `http://localhost:9005`
- **UserURL**: `http://localhost:9005`
- **GatewayURL**: `http://localhost:9001`
- **PaymentUrl**: `http://localhost:9004`

## TraderService APIs
- **POST /products**  
  Saves a product.  
  **Body**: `{ "name": string, "description": string, "price": number, "stock": number }`  
  **Example**: `{ "name": "Hihi Cola", "description": "An amazing drink", "price": 19, "stock": 10 }`

- **GET /products**  
  Retrieves all products, optionally filtered by company.  
  **Query Param**: `company` (optional, e.g., `Giza`)  

- **GET /products/{id}**  
  Fetches a product by ID.  
  **Example**: `/products/6`

- **GET /products/{id}/stock?quantity={n}**  
  Checks if a product has sufficient stock.  
  **Query Param**: `quantity` (e.g., `1`)  
  **Example**: `/products/1/stock?quantity=1`

- **PUT /products/{id}**  
  Updates a product's details.  
  **Body**: `{ "name": string, "description": string, "price": number }`  
  **Example**: `/products/5`, `{ "name": "Alex Cola", "description": "An amazing drink", "price": 19 }`

- **DELETE /products/{id}**  
  Deletes a product by ID.  
  **Example**: `/products/3`

## UserService APIs
- **POST /register**  
  Registers a new user.  
  **Body**: `{ "username": string, "email": string, "password": string }`  
  **Example**: `{ "username": "testuser", "email": "test@example.com", "password": "password123" }`

- **POST /login**  
  Authenticates a user.  
  **Body**: `{ "username": string, "password": string }`  
  **Example**: `{ "username": "testuser", "password": "password123" }`

- **GET /{id}/validate**  
  Validates a user by ID.  
  **Example**: `/1/validate`

## OrderService APIs
- **POST /orders**  
  Creates a new order.  
  **Body**: `{ "userId": number, "productId": number, "quantity": number }`  
  **Example**: `{ "userId": 1, "productId": 9, "quantity": 2 }`

- **GET /orders/user/{id}**  
  Retrieves all orders for a user.  
  **Example**: `/orders/user/1`

- **GET /orders/{id}/status**  
  Checks the status of an order.  
  **Example**: `http://localhost:9001/orders/1/status`

## PaymentService APIs
- **POST /payments**  
  Processes a payment.  
  **Body**: `{ "orderId": number, "userId": number, "amount": number, "currency": string, "paymentMethod": string, "paypalEmail": string, "description": string }`  
  **Example**: `{ "orderId": 2, "userId": 1, "amount": 99.99, "currency": "USD", "paymentMethod": "PAYPAL", "paypalEmail": "user@example.com", "description": "Order payment" }`

- **POST /payments/{id}/refund**  
  Issues a refund for a payment.  
  **Body**: Same as POST /payments  
  **Example**: `http://localhost:9004/payments/103/refund`

- **GET /payments/health**  
  Checks the health status of the payment service.  
  **Example**: `http://localhost:9004/payments/health`

- **GET (Order Status)**  
  Intended to check order status (endpoint incomplete in provided collection).

## Notes
- All APIs use `noauth` authentication unless specified otherwise.
- Replace `{{TraderURL}}`, `{{UserURL}}`, `{{GatewayURL}}`, and `{{PaymentUrl}}` with the respective base URLs.
- Ensure proper JSON formatting for request bodies.
- The PaymentService uses `http://localhost:9004` for most endpoints, with some hardcoded URLs.
- For further details, refer to the Postman collection: [Beemo Collection](https://gold-resonance-166177.postman.co/workspace/613b5506-7453-4bfb-82e1-c499336d62d8/collection/43221147-000d6693-beb0-47e7-8137-044526183f06?action=share&source=collection_link&creator=43221147).
---
## 🔐 Security

A mock `noauth` HTTP scheme is defined in OpenAPI. You can implement full JWT authentication later.

---

## 📄 .gitignore

Common `.gitignore` entries:
* `target/`
* `.idea/`, `.vscode/`
* `.env`, `*.log`
* `*.iml`

---

## 📚 Tech Stack

* Java 17
* Spring Boot
* Spring Cloud (Gateway, Eureka)
* Maven
* Postman
* OpenAPI 3.0

---

## 🧪 Future Enhancements

* ✅ Inventory management
* ✅ Kafka or RabbitMQ for event-driven orders

---

## 🐝 About

**Beemo** was created as a sample e-commerce platform with learning in mind—focused on testing, scalability, and modularity in Spring Boot microservices.


## 📎 License

This project is licensed under the MIT License.



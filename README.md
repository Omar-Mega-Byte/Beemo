
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
```


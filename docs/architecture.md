# Architecture Overview

### ✅ Layered Architecture

- Follows conventional architecture: `Controller → Service → Repository`.
- Project includes **one API** in **Spring Boot**.
- Plans to experiment with **DDD** or **Clean Architecture**.

### ✅ Documentation

- Uses **Springdoc** for automatic Swagger documentation in Spring Boot.

### ✅ Elasticsearch

- Used to store transaction history for analytics and search features.
- Still saves to a relational database for financial audit purposes.

### 🔁 Batch/Bulk Operations

- Messaging tech (likely **RabbitMQ**) will be used.
- Exploring **Spring Batch** as a potential solution.
- Planning to handle large transaction volumes for seasonal peaks (e.g., **Black Friday**, **Spring Sales**).
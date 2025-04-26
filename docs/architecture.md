# Architecture Overview

### ✅ Layered Architecture

- Follows conventional architecture: `Controller → Service → Repository`.
- Project includes **one API** in **Spring Boot**.
- Plans to experiment with **DDD** or **Clean Architecture**.

### ✅ Documentation

- Uses **Springdoc** for automatic Swagger documentation in Spring Boot.

### ✅ Security

- All requests are protected by **Spring Security and JJWT**. The user must be authenticated to make endpoint requests via token.

### 🔁 Batch/Bulk Operations

- Messaging tech will be used ✅ 
- Exploring **Spring Batch** as a potential solution.
- Planning to handle large transaction volumes for seasonal peaks (e.g., **Black Friday**, **Spring Sales**).
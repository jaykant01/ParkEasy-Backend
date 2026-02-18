<p align="center">
  <img src="https://spring.io/img/projects/spring-boot.svg" alt="Spring Boot Logo" width="120"/>
</p>

<h1 align="center">🚗 ParkEasy Backend</h1>

<p align="center">
  Smart Parking Management REST API built with Spring Boot
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java"/>
  <img src="https://img.shields.io/badge/SpringBoot-3.x-brightgreen?style=for-the-badge&logo=springboot"/>
  <img src="https://img.shields.io/badge/Database-PostgreSQL-blue?style=for-the-badge&logo=postgresql"/>
  <img src="https://img.shields.io/badge/Security-JWT-red?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Build-Maven-purple?style=for-the-badge&logo=apachemaven"/>
</p>

---

## 📌 Overview

ParkEasy Backend is a scalable and secure **Spring Boot REST API** that powers a smart parking management system.

It provides:

- 🔐 JWT Authentication & Authorization
- 👤 User Management
- 🅿️ Parking Lot & Slot Management
- 📅 Booking System Future Implementation
- ☁️ AWS S3 Image Upload Support
- 🛡 Role-based Access Control
- 📊 Admin Operations

---

## 🏗 Tech Stack

| Layer        | Technology |
|--------------|------------|
| Language     | Java 17+ |
| Framework    | Spring Boot 3.x |
| Security     | Spring Security + JWT |
| ORM          | Hibernate / JPA |
| Database     | PostgreSQL / MySQL |
| Storage      | AWS S3 |
| Build Tool   | Maven |
| Docs         | Swagger / OpenAPI |

---

## 🗄 Database Configuration

Update your `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```
---
## 🚀 Running the Project
git clone https://github.com/your-username/parkeasy-backend.git
cd parkeasy-backend

mvn clean install

mvn spring-boot:run

---
## 🛡 Security Features

BCrypt Password Hashing

JWT Token Filter

Role-based Endpoint Access

Global Exception Handling

Input Validation (@Valid)

CORS Configuration

SQL Injection Protection via JPA

## 👨‍💻 Author

Jaykant Yadav
Backend Developer

# 🍔 CraveX — Food Delivery SaaS Platform

> A production-ready, multi-role food delivery platform built with Spring Boot, PostgreSQL, Redis, and RabbitMQ.

---

## 📌 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Features](#features)
- [Entity Design](#entity-design)
- [API Endpoints](#api-endpoints)
- [Authentication Flow](#authentication-flow)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [Database Setup](#database-setup)
- [Email Service](#email-service)
- [Error Handling](#error-handling)
- [Deployment](#deployment)

---

## Overview

**CraveX** is a full-featured food delivery SaaS application that connects **customers**, **restaurants**, and **delivery agents** on a single platform. Built as a monolith with clean service boundaries that support future microservices migration.

The platform supports:
- Multi-role registration with OTP email verification
- Role-based access control (RBAC) using JWT
- Real-time order tracking
- Async email notifications via RabbitMQ
- Redis-backed OTP storage with TTL and resend limits
- Payment processing with Razorpay integration

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.0.6 |
| **Database** | PostgreSQL 16 (Neon — free cloud tier) |
| **Cache / OTP Store** | Redis (Docker) |
| **Message Queue** | RabbitMQ (async email) |
| **Email Provider** | Brevo SMTP (300 free emails/day) |
| **Security** | Spring Security + JWT (JJWT 0.12.6) |
| **ORM** | Hibernate 7 / Spring Data JPA |
| **API Docs** | SpringDoc OpenAPI (Swagger UI) |
| **Validation** | Jakarta Bean Validation |
| **Build Tool** | Maven |
| **Dev Tools** | Spring Boot DevTools, Lombok |
| **IDE** | IntelliJ IDEA Ultimate |
| **Containerization** | Docker + Docker Compose |
| **Frontend Hosting** | Vercel |
| **Backend Hosting** | Render (free tier) |

---

## System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     Client (React)                      │
│                    Hosted on Vercel                     │
└─────────────────────────┬───────────────────────────────┘
                          │ HTTP/HTTPS
                          ▼
┌─────────────────────────────────────────────────────────┐
│              Spring Boot REST API                       │
│               Hosted on Render                          │
│                                                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────────┐  │
│  │   Auth   │  │ Customer │  │     Restaurant       │  │
│  │ Service  │  │ Service  │  │      Service         │  │
│  └────┬─────┘  └────┬─────┘  └──────────┬───────────┘  │
│       │              │                   │              │
│  ┌────▼──────────────▼───────────────────▼───────────┐  │
│  │              Spring Security + JWT                 │  │
│  └────────────────────────────────────────────────────┘  │
└────────────┬────────────────┬────────────┬──────────────┘
             │                │            │
             ▼                ▼            ▼
    ┌─────────────┐  ┌──────────────┐  ┌──────────────┐
    │ PostgreSQL  │  │    Redis     │  │  RabbitMQ    │
    │  (Neon)     │  │ OTP Storage  │  │ Email Queue  │
    │  Main DB    │  │  TTL: 5min   │  │              │
    └─────────────┘  └──────────────┘  └──────┬───────┘
                                              │
                                              ▼
                                    ┌──────────────────┐
                                    │   Brevo SMTP     │
                                    │ (Email Delivery) │
                                    └──────────────────┘
```

---

## Features

### Authentication & Authorization
- ✅ Multi-role registration: **Customer**, **Restaurant**, **Delivery Agent**
- ✅ OTP-based email verification (5-minute TTL via Redis)
- ✅ OTP resend with rate limiting (max 3 attempts per hour)
- ✅ JWT access token + Refresh token (30-day expiry)
- ✅ Role-based access control (`CUSTOMER`, `RESTAURANT`, `DELIVERY_AGENT`, `ADMIN`)
- ✅ BCrypt password hashing
- ✅ Custom exception handling with error codes

### Customer
- ✅ Register with address and GPS coordinates
- ✅ Browse restaurants
- ✅ Place orders with multiple items
- ✅ Real-time order tracking
- ✅ Digital wallet

### Restaurant
- ✅ Register with full restaurant profile (name, address, timings, GPS)
- ✅ Menu management (add, update, toggle availability)
- ✅ Receive and manage incoming orders
- ✅ Commission-based platform integration (10%)

### Delivery Agent
- ✅ Register with vehicle details
- ✅ Toggle availability status
- ✅ Accept and complete deliveries
- ✅ Real-time GPS tracking
- ✅ Earnings wallet

### Notifications
- ✅ OTP verification email
- ✅ Welcome email on registration
- ✅ Order status emails (placed, confirmed, out for delivery, delivered)
- ✅ All emails sent asynchronously via RabbitMQ → Brevo SMTP

---

## Entity Design

CraveX uses a **User as base identity** pattern — all roles share one `users` table for authentication, with separate profile tables per role.

```
users (authentication base)
  ├── customers     (1:1 → users.id)
  ├── restaurants   (1:1 → users.id)
  └── delivery_agents (1:1 → users.id)

restaurants
  └── menu_items    (1:Many → restaurants.id)

orders (core transaction table)
  ├── customer_id   → customers.id
  ├── restaurant_id → restaurants.id
  └── delivery_agent_id → delivery_agents.id

orders
  ├── order_items   (1:Many → orders.id)
  ├── payments      (1:1   → orders.id)
  ├── order_tracking (1:Many → orders.id)
  └── notifications  (1:Many → orders.id)
```

### Core Entities

| Entity | Table | Purpose |
|---|---|---|
| `User` | `users` | Authentication, role assignment |
| `Customer` | `customers` | Customer profile, wallet, address |
| `Restaurant` | `restaurants` | Restaurant profile, menu, hours |
| `DeliveryAgent` | `delivery_agents` | Agent profile, vehicle, location |
| `FoodMenu` | `menu_items` | Menu items per restaurant |
| `Order` | `orders` | Core transaction |
| `OrderHistory` | `order_history` | Order status state machine |
| `OrderTracking` | `order_tracking` | Real-time GPS snapshots |
| `Payment` | `payments` | Payment details, Razorpay IDs |
| `Notification` | `notifications` | Notification audit trail |

---

## API Endpoints

### Auth

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `POST` | `/api/auth/register/customer` | Register as customer | Public |
| `POST` | `/api/auth/register/restaurant` | Register as restaurant | Public |
| `POST` | `/api/auth/register/delivery-agent` | Register as delivery agent | Public |
| `POST` | `/api/auth/verify-otp` | Verify OTP and activate account | Public |
| `POST` | `/api/auth/resend-otp?email=` | Resend OTP (max 3/hour) | Public |
| `POST` | `/api/auth/login` | Login and get JWT | Public |
| `POST` | `/api/auth/refresh-token` | Get new access token | Public |
| `POST` | `/api/auth/logout` | Revoke refresh token | Bearer |

### Customer

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `GET` | `/api/customers/profile` | Get customer profile | CUSTOMER |
| `PUT` | `/api/customers/profile` | Update address/location | CUSTOMER |
| `GET` | `/api/customers/orders` | Get order history | CUSTOMER |

### Restaurant

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `GET` | `/api/restaurants` | Get all open restaurants | CUSTOMER |
| `GET` | `/api/restaurants/{id}/menu` | Get restaurant menu | CUSTOMER |
| `GET` | `/api/restaurants/profile` | Get own profile | RESTAURANT |
| `PUT` | `/api/restaurants/profile` | Update restaurant details | RESTAURANT |
| `POST` | `/api/restaurants/menu` | Add menu item | RESTAURANT |
| `PUT` | `/api/restaurants/menu/{id}` | Update menu item | RESTAURANT |
| `PATCH` | `/api/restaurants/menu/{id}/toggle` | Toggle item availability | RESTAURANT |
| `GET` | `/api/restaurants/orders` | Get incoming orders | RESTAURANT |
| `PATCH` | `/api/restaurants/orders/{id}/confirm` | Confirm order | RESTAURANT |

### Order

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `POST` | `/api/orders` | Place new order | CUSTOMER |
| `GET` | `/api/orders/{id}` | Get order details | CUSTOMER |
| `GET` | `/api/orders/{id}/tracking` | Get live tracking | CUSTOMER |
| `DELETE` | `/api/orders/{id}` | Cancel order | CUSTOMER |

### Delivery Agent

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `GET` | `/api/delivery/profile` | Get agent profile | DELIVERY_AGENT |
| `PATCH` | `/api/delivery/availability` | Toggle availability | DELIVERY_AGENT |
| `GET` | `/api/delivery/orders` | Get assigned orders | DELIVERY_AGENT |
| `PATCH` | `/api/delivery/orders/{id}/status` | Update delivery status | DELIVERY_AGENT |
| `POST` | `/api/delivery/tracking` | Push GPS location | DELIVERY_AGENT |

---

## Authentication Flow

### Registration (all roles)

```
1. POST /api/auth/register/{role}
   Body: { email, password, phone, firstName, ...roleFields }
        ↓
2. AuthService validates input
   Checks duplicate email + phone
   Checks resend limit (Redis)
        ↓
3. Generates OTP (6-digit)
   Stores full registration data in Redis (TTL: 5 min)
   Sends OTP email via RabbitMQ → Brevo
        ↓
4. POST /api/auth/verify-otp
   Body: { email, otp }
        ↓
5. AuthService reads Redis
   Validates OTP + expiry
   Saves User to users table
   Routes to role service (Customer/Restaurant/Agent)
   Role service saves to its own table
   Deletes Redis key
   Sends welcome email
        ↓
6. Returns: { accessToken, refreshToken, userType }
```

### OTP Resend Limits

```
Per email, per hour: max 3 resend attempts
Tracked in Redis with key: "resend:{email}"
TTL: 1 hour
Exceeding limit → HTTP 429 Too Many Requests
```

### JWT Structure

```json
{
  "sub": "john@gmail.com",
  "role": "CUSTOMER",
  "iat": 1717500000,
  "exp": 1717586400
}
```

Access token validity: **24 hours**
Refresh token validity: **30 days** (stored in `refresh_tokens` table)

---

## Project Structure

```
src/main/java/com/carve/cravex/
├── config/
│   ├── RabbitMQConfig.java        ← queues, exchanges, routing keys
│   ├── RedisConfig.java           ← Redis template config
│   ├── SecurityConfig.java        ← Spring Security filter chain
│   └── AppConfig.java             ← PasswordEncoder, etc.
│
├── controller/
│   ├── AuthController.java
│   ├── CustomerController.java
│   ├── RestaurantController.java
│   ├── OrderController.java
│   └── DeliveryAgentController.java
│
├── service/
│   ├── AuthService.java
│   ├── CustomerService.java
│   ├── RestaurantService.java
│   ├── OrderService.java
│   └── DeliveryAgentService.java
│
├── serviceimpl/
│   ├── AuthServiceImpl.java       ← OTP, registration, login
│   ├── CustomerServiceImpl.java
│   ├── RestaurantServiceImpl.java
│   ├── OrderServiceImpl.java
│   └── DeliveryAgentServiceImpl.java
│
├── entity/
│   ├── User.java
│   ├── Customer.java
│   ├── Restaurant.java
│   ├── DeliveryAgent.java
│   ├── FoodMenu.java
│   ├── Order.java
│   ├── OrderHistory.java
│   ├── OrderTracking.java
│   ├── Payment.java
│   ├── Notification.java
│   └── RefreshToken.java
│
├── dto/
│   ├── request/
│   │   └── auth/
│   │       ├── CustomerRegisterRequest.java
│   │       ├── RestaurantRegisterRequest.java
│   │       └── DeliveryAgentRegisterRequest.java
│   ├── response/
│   │   ├── ErrorResponse.java
│   │   └── AuthResponse.java
│   └── RegistrationTempDto.java   ← stored in Redis during OTP wait
│
├── mappers/
│   ├── CustomerMapper.java
│   ├── RestaurantModelMapper.java
│   └── DeliveryAgentMapper.java
│
├── repository/
│   ├── UserRepository.java
│   ├── CustomerRepository.java
│   ├── RestaurantRepository.java
│   ├── OrderRepository.java
│   ├── DeliveryAgentRepository.java
│   └── RefreshTokenRepository.java
│
├── event/
│   └── EmailEvent.java            ← RabbitMQ message payload
│
├── eventlisteners/
│   └── OtpEmailListeners.java     ← consumes RabbitMQ queue
│
├── exceptions/
│   ├── base/
│   │   └── CraveXException.java
│   ├── auth/
│   │   ├── EmailAlreadyExistsException.java
│   │   ├── PhoneAlreadyExistsException.java
│   │   ├── InvalidOtpException.java
│   │   ├── OtpExpiredException.java
│   │   ├── OtpResendLimitException.java
│   │   ├── SessionNotFoundException.java
│   │   └── InvalidTokenException.java
│   ├── user/
│   │   └── UserNotFoundException.java
│   └── GlobalExceptionHandler.java
│
├── enums/
│   ├── UserRole.java              ← CUSTOMER, RESTAURANT, DELIVERY_AGENT, ADMIN
│   └── VehicleType.java           ← BIKE, SCOOTER, CAR
│
├── util/
│   ├── OtpGenerator.java
│   ├── EmailBuilderUtil.java      ← HTML email templates
│   └── JwtUtil.java
│
└── CraveXApplication.java
```

---

## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker Desktop
- IntelliJ IDEA (recommended)

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/cravex.git
cd cravex
```

### 2. Start PostgreSQL and Redis via Docker

```bash
docker compose up -d
```

This starts:
- PostgreSQL on port `5432`
- Redis on port `6379`
- RabbitMQ on port `5672` (management UI: `15672`)

### 3. Configure Environment Variables

Copy the example env file and fill in your values:

```bash
cp .env.example .env
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

Or run `CraveXApplication.java` directly from IntelliJ.

### 5. Access Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

## Environment Variables

Create a `.env` file or set these in your IDE run configuration:

```properties
# Database (PostgreSQL on Neon for production)
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/cravex?TimeZone=Asia/Kolkata
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password

# Redis
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# RabbitMQ
SPRING_RABBITMQ_HOST=localhost
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=username
SPRING_RABBITMQ_PASSWORD=password

# JWT
JWT_SECRET=your_256_bit_secret_key_here
JWT_EXPIRATION_MS=86400000

# Brevo SMTP (Email)
SPRING_MAIL_HOST=host
SPRING_MAIL_PORT=port
SPRING_MAIL_USERNAME=your_brevo_login_email
SPRING_MAIL_PASSWORD=your_brevo_smtp_key
```

---

## Database Setup

### Local Development (Docker)

```yaml
# docker-compose.yml

services:
  postgres:
    image: postgres:16
    container_name: cravex-db
    environment:
      POSTGRES_DB: cravex
      POSTGRES_HOST_AUTH_METHOD: trust
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine
    container_name: cravex-redis
    ports:
      - "6379:6379"

  rabbitmq:
    image: rabbitmq:3-management
    container_name: cravex-rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"

volumes:
  pgdata:
```

### Table Creation

Tables are created automatically by Hibernate on application startup via:

```properties
spring.jpa.hibernate.ddl-auto=update
```

No manual SQL required.

### Production Database

CraveX uses **Neon** (free PostgreSQL cloud hosting):

```properties
spring.datasource.url=jdbc:postgresql://ep-xxx.neon.tech/cravex?sslmode=require&TimeZone=Asia/Kolkata
```

---

## Email Service

CraveX uses **Brevo** (formerly Sendinblue) as the SMTP provider.

Free tier: **300 emails/day** — sufficient for early stage.

### Email Types

| Email | Trigger | Queue |
|---|---|---|
| OTP Verification | On registration | `email.otp` |
| Welcome Email | After OTP verified | `email.welcome` |
| Order Placed | Customer places order | `email.order` |
| Order Confirmed | Restaurant confirms | `email.order` |
| Out for Delivery | Agent picks up | `email.order` |
| Order Delivered | Delivery complete | `email.order` |

### RabbitMQ Setup

```
Exchange: cravex.email.exchange (DirectExchange)
Queues:
  cravex.email.otp.queue     → routing key: email.otp
  cravex.email.welcome.queue → routing key: email.welcome
  cravex.email.order.queue   → routing key: email.order
```

---

## Error Handling

All errors return a consistent JSON structure:

```json
{
    "errorCode": "AUTH_001",
    "message": "Email already registered: john@gmail.com",
    "status": 409,
    "timestamp": "2026-06-05 10:30:00",
    "fieldErrors": null
}
```

### Error Codes

| Code | Meaning | HTTP Status |
|---|---|---|
| `AUTH_001` | Email already registered | 409 Conflict |
| `AUTH_002` | Phone already registered | 409 Conflict |
| `AUTH_003` | Invalid OTP | 400 Bad Request |
| `AUTH_004` | OTP expired | 410 Gone |
| `AUTH_005` | OTP resend limit exceeded | 429 Too Many Requests |
| `AUTH_006` | Registration session not found | 404 Not Found |
| `AUTH_007` | Invalid or expired token | 401 Unauthorized |
| `USER_001` | User not found | 404 Not Found |
| `VALIDATION_001` | Input validation failed | 400 Bad Request |
| `INTERNAL_001` | Unexpected server error | 500 Internal Server Error |

---

## Deployment

### Backend — Render (Free)

```yaml
# render.yaml

services:
  - type: web
    name: cravex-api
    env: java
    buildCommand: ./mvnw package -DskipTests
    startCommand: java -Duser.timezone=Asia/Kolkata -jar target/CraveX-0.0.1-SNAPSHOT.jar
    envVars:
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: SPRING_DATASOURCE_URL
        fromDatabase:
          name: cravex-db
          property: connectionString
```

### Frontend — Vercel

```json
{
  "rewrites": [
    {
      "source": "/api/:path*",
      "destination": "https://cravex-api.onrender.com/:path*"
    }
  ]
}
```

### Keep Render Awake

Render free tier spins down after 15 minutes of inactivity.
Add your health check URL to **UptimeRobot** (free) to ping every 10 minutes:

```
https://cravex-api.onrender.com/actuator/health
```

---

## Developer

**Shreemansu**
B.Sc. Information Science & Telecommunication
Ravenshaw University, Odisha

- Java Backend Developer
- Spring Boot | PostgreSQL | Redis | RabbitMQ
- Building CraveX as a production SaaS project

---

## License

This project is built for learning and portfolio purposes.

---

*Built with ☕ and Spring Boot*

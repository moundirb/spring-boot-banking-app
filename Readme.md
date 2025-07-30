# 💳 Banking App (Spring Boot)

A simple banking application built with Spring Boot that allows users to create accounts, update them, and send email alerts. Built using clean code practices, layered architecture (Controller, Service, Repository, DTO, Utils), and Docker for easy deployment.

---

## 🚀 Features

- ✅ Create user accounts with unique account numbers
- 🔍 Get user by email or account number
- ✏️ Update user details
- ❌ Delete accounts
- 📬 Send email alerts on account actions
- 🧪 Tested with Postman
- 🐳 Docker + MySQL integration

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Lombok
- MySQL
- Docker & Docker Compose
- JavaMailSender

---

## 📦 Project Structure

```text
src
├── controller         → API endpoints
├── dto                → Request and response objects
├── entity             → JPA entities (User, Account)
├── repository         → Spring Data repositories
├── service            
│   ├── impl           → Business logic implementations
│   ├── UserService    → User service interface
│   └── EmailService   → Mail service interface
├── utils              → Utility classes (e.g., AccountUtils)
└── config             → Mail and DB configs
```

---

## 📥 Sample Request (POST /api/v1/user)

```json
{
  "firstname": "John",
  "lastname": "Doe",
  "othername": "Michael",
  "gender": "Male",
  "address": "123 Maple Street, New York, NY",
  "stateOfOrigin": "New York",
  "email": "john.doe@example.com",
  "phoneNumber": "1234567890",
  "alterPhoneNumber": "0987654321"
}

```

---

## 🧱 Docker Setup

Make sure Docker is installed and running, then use:

```bash
docker-compose up --build
```

This sets up both the Spring Boot app and MySQL container.

---

## ✨ Highlights

* **Builder Pattern**: Used to create clean and safe object instances (from Gang of Four).
* **Account Number Generator**: Combines current year and a random 6-digit number.
* **Email Service**: Sends alerts using Spring Boot’s JavaMailSender.

---

## 📝 To Do

* [ ] Add PATCH endpoint using JSON Merge Patch
* [ ] Improve email templates
* [ ] Add Swagger/OpenAPI documentation



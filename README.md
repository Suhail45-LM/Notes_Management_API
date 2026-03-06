# 📝 Notes Management API

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Authentication-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-API_Docs-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

**A secure, scalable, and production-ready REST API for personal notes management**

*Built with Java 17 · Spring Boot 3 · JWT Authentication · PostgreSQL · Swagger UI*

[📖 API Docs](#-api-documentation) · [🚀 Quick Start](#-installation--setup) · [📌 Endpoints](#-api-endpoints) · [👤 Author](#-author)

</div>

---

## 📋 Table of Contents

- [Project Description](#-project-description)
- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Project Architecture](#-project-architecture)
- [Database Design](#-database-design)
- [API Endpoints](#-api-endpoints)
- [Authentication Flow](#-authentication-flow)
- [API Documentation](#-api-documentation)
- [Installation & Setup](#-installation--setup)
- [Example API Usage](#-example-api-usage)
- [Future Improvements](#-future-improvements)
- [Author](#-author)
- [License](#-license)

---

## 📖 Project Description

**Notes Management API** is a fully secured backend REST API that enables users to register, authenticate, and manage their personal notes through a clean, well-structured, and documented interface.

In today's digital world, people need a reliable and private way to store and manage information. This project solves a real-world problem — **providing each user their own private, secure note-taking system** — backed by industry-standard security practices like JWT authentication and BCrypt password hashing.

### 🌍 Real-World Problem It Solves

> Imagine a note-taking application where every user's data is completely private, passwords are never exposed even if the database is compromised, and every API call is protected by a cryptographically signed token. That is exactly what this project demonstrates — a backend system designed the way real production applications are built.

### What This Project Demonstrates

- How **stateless JWT authentication** works in a real REST API
- How **ownership-based data access** is enforced at the service layer
- How **layered architecture** properly separates concerns in a Spring Boot application
- How **Swagger UI** serves as live, interactive API documentation

---

## ✨ Key Features

| Feature | Description |
|---|---|
| 🔐 **User Registration** | New users can create an account with a username, email, and password |
| 🔑 **User Login** | Authenticated users receive a cryptographically signed JWT token |
| 🛡️ **JWT Authentication** | Every protected request is validated using a stateless Bearer token |
| 📝 **Create Notes** | Authenticated users can create personal notes with a title and content |
| ✏️ **Update Notes** | Users can edit their own notes — ownership is strictly enforced at the service layer |
| 🗑️ **Delete Notes** | Users can delete their notes; admins can delete any note in the system |
| 👁️ **View Personal Notes** | Users see only their own notes; admins can view all notes |
| 🔰 **Role-Based Access Control** | Two roles — `USER` and `ADMIN` — with clearly defined access boundaries |
| 🔒 **Secure Password Hashing** | Passwords are hashed using BCrypt — never stored or returned in plain text |
| 🌐 **Swagger UI** | Interactive, browser-based API documentation for live endpoint testing |
| ✅ **Input Validation** | All request fields are validated with descriptive, field-level error messages |
| 📦 **Standard Response Format** | Every response follows a consistent `{ timestamp, status, message, data }` envelope |

---

## 🛠️ Tech Stack

| Technology | Version | Why It Was Used |
|---|---|---|
| **Java** | 17 | Long-term support version with modern features; the industry standard for backend APIs |
| **Spring Boot** | 3.2.5 | Reduces boilerplate drastically; auto-configures most components; production-ready out of the box |
| **Spring Security** | 6.x | Provides a robust security filter chain, stateless session management, and method-level authorization |
| **Spring Data JPA** | 3.x | Eliminates boilerplate SQL; maps Java classes to database tables through Hibernate |
| **Hibernate** | 6.x | ORM that generates and executes SQL from Java entity class definitions |
| **PostgreSQL** | 15 | Open-source, reliable relational database with native UUID support and excellent JPA compatibility |
| **JWT (JJWT)** | 0.12.5 | Enables stateless authentication — no server-side session storage needed, scales horizontally |
| **BCrypt** | — | One-way password hashing with built-in salting; immune to rainbow table attacks by design |
| **MapStruct** | 1.5.5 | Compile-time DTO mapper — zero reflection overhead, fully type-safe |
| **Lombok** | 1.18.32 | Reduces boilerplate code (getters, setters, builders, constructors) via compile-time annotations |
| **SpringDoc OpenAPI** | 2.5.0 | Auto-generates interactive Swagger UI documentation from controller annotations |
| **Maven** | 3.9+ | Dependency management and build lifecycle management |
| **HikariCP** | — | High-performance JDBC connection pool — included automatically with Spring Boot |

---

## 🏗️ Project Architecture

This project follows a strict **Layered Architecture** (N-Tier). Each layer has one clear responsibility and communicates only with the layer directly below it — a principle known as Separation of Concerns.

```
┌──────────────────────────────────────────────────────────────┐
│                     CLIENT / SWAGGER UI                      │
│                (HTTP Requests & Responses)                   │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│               SPRING SECURITY FILTER CHAIN                   │
│           JwtAuthenticationFilter runs here                  │
│       Validates Bearer token on every incoming request       │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                    CONTROLLER LAYER                          │
│            AuthController  ·  NoteController                 │
│    Handles HTTP routing, input parsing, and response format  │
│             Zero business logic lives here                   │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                     SERVICE LAYER                            │
│          AuthServiceImpl  ·  NoteServiceImpl                 │
│   Ownership checks, role enforcement, transaction control,   │
│         all business rules are decided here                  │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                   REPOSITORY LAYER                           │
│          UserRepository  ·  NoteRepository                   │
│    Spring Data JPA interfaces — Hibernate auto-generates     │
│              all SQL queries from method names               │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                      DATABASE                                │
│                PostgreSQL  —  notes_db                       │
│                 Tables: users  ·  notes                      │
└──────────────────────────────────────────────────────────────┘
```

### Package Responsibilities

| Package | Responsibility |
|---|---|
| `controller/` | Receives HTTP requests, validates input with `@Valid`, delegates to service, returns `ResponseEntity` |
| `service/impl/` | Contains ALL business rules — ownership checks, role enforcement, data orchestration |
| `repository/` | Database access only — pure Spring Data JPA interfaces, zero SQL written manually |
| `entity/` | JPA-mapped Java classes that represent database tables — Hibernate reads these to generate SQL |
| `dto/` | Request and response shapes that decouple the API contract from the internal database model |
| `security/` | JWT filter, Spring Security configuration, and user loading from the database |
| `exception/` | Custom exception classes and a global handler ensuring consistent error response shapes |
| `config/` | Swagger/OpenAPI configuration and application-level Spring beans |
| `util/` | Stateless helper utilities — `SecurityUtils` retrieves the currently authenticated user |

---

## 🗄️ Database Design

### Schema Diagram

```
┌──────────────────────────────┐          ┌───────────────────────────────┐
│            USERS             │          │            NOTES              │
├──────────────────────────────┤          ├───────────────────────────────┤
│ PK  id           UUID        │──┐       │ PK  id           UUID         │
│     username     VARCHAR(50) │  │       │     title        VARCHAR(255) │
│     email        VARCHAR(100)│  │ 1 : N │     content      TEXT         │
│     password     VARCHAR(255)│  └──────▶│ FK  user_id      UUID         │
│     role         VARCHAR(20) │          │     created_at   TIMESTAMP    │
│     created_at   TIMESTAMP   │          │     updated_at   TIMESTAMP    │
│     updated_at   TIMESTAMP   │          └───────────────────────────────┘
└──────────────────────────────┘

Relationship: One User → Many Notes  (1 : N)
notes.user_id is a FOREIGN KEY referencing users.id
```

### Users Table

| Column | Type | Constraint | Purpose |
|---|---|---|---|
| `id` | UUID | Primary Key | Auto-generated — UUID prevents sequential ID guessing attacks |
| `username` | VARCHAR(50) | Unique, Not Null | Login identifier — automatically indexed by unique constraint |
| `email` | VARCHAR(100) | Unique, Not Null | Validated email format — automatically indexed |
| `password` | VARCHAR(255) | Not Null | Always stores BCrypt hash (~60 chars) — raw password never saved |
| `role` | VARCHAR(20) | Not Null | `USER` or `ADMIN` — stored as string for human readability |
| `created_at` | TIMESTAMP | Not Null | Auto-populated once on insert by JPA auditing |
| `updated_at` | TIMESTAMP | Not Null | Auto-updated on every save by JPA auditing |

### Notes Table

| Column | Type | Constraint | Purpose |
|---|---|---|---|
| `id` | UUID | Primary Key | Random UUID — prevents note ID enumeration |
| `title` | VARCHAR(255) | Not Null | Note heading — validated at DTO level |
| `content` | TEXT | Not Null | Note body — no length limit (PostgreSQL TEXT stores up to 1 GB) |
| `user_id` | UUID | Foreign Key | References `users.id` — links every note to its owner |
| `created_at` | TIMESTAMP | Not Null | Auto-set on first insert |
| `updated_at` | TIMESTAMP | Not Null | Auto-updated on every modification |

---

## 📌 API Endpoints

### 🔑 Authentication

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | ❌ Public | Register a new user account |
| `POST` | `/api/auth/login` | ❌ Public | Login and receive a JWT token |

### 📝 Notes

| Method | Endpoint | Auth | Who Can Access | Description |
|---|---|---|---|---|
| `POST` | `/api/notes` | ✅ Token | USER / ADMIN | Create a new note |
| `GET` | `/api/notes` | ✅ Token | USER (own) / ADMIN (all) | Get notes |
| `GET` | `/api/notes/{id}` | ✅ Token | USER (own) / ADMIN | Get one note by ID |
| `PUT` | `/api/notes/{id}` | ✅ Token | Owner only | Update a note |
| `DELETE` | `/api/notes/{id}` | ✅ Token | USER (own) / ADMIN (any) | Delete a note |

### Role Access Matrix

| Action | USER | ADMIN |
|---|---|---|
| Register / Login | ✅ | ✅ |
| Create a note | ✅ | ✅ |
| View own notes | ✅ | ✅ |
| View ALL notes in system | ❌ | ✅ |
| Update own note | ✅ | ❌ |
| Delete own note | ✅ | ✅ |
| Delete any user's note | ❌ | ✅ |

---

## 🔐 Authentication Flow

This API uses **stateless JWT (JSON Web Token) authentication**. The server stores zero session state — every request carries its own authentication information inside the token itself.

```
REGISTRATION
────────────────────────────────────────────────────
1. Client sends  →  POST /api/auth/register
                    { username, email, password }

2. Server        →  Validates all input fields
                →  Checks username is not already taken
                →  Checks email is not already registered
                →  Hashes password using BCrypt (10 rounds)
                →  Saves user to database with role = USER
                →  Generates a signed JWT token
                →  Returns 201 Created with token + user details


LOGIN
────────────────────────────────────────────────────
1. Client sends  →  POST /api/auth/login
                    { username, password }

2. Server        →  Loads user from database by username
                →  BCrypt.matches(rawPassword, storedHash)
                →  If MATCH:   generate JWT → return 200
                →  If NO MATCH: return 401 Unauthorized


ACCESSING A PROTECTED ENDPOINT
────────────────────────────────────────────────────
1. Client sends  →  GET /api/notes
                    Header: Authorization: Bearer eyJhbGci...

2. JwtFilter     →  Extracts token from Authorization header
                →  Validates cryptographic signature
                →  Checks token has not expired
                →  Loads user from database by username in token
                →  Stores user in SecurityContextHolder (ThreadLocal)

3. Controller    →  Request reaches NoteController
4. Service       →  Fetches only this user's notes from database
5. Response      →  Returns 200 OK with the notes list
```

### JWT Token Structure

```
eyJhbGciOiJIUzI1NiJ9           ← Header:    { "alg": "HS256" }
.
eyJzdWIiOiJzdWhhaWxfZGV2In0    ← Payload:   { "sub": "suhail_dev", "role": "ROLE_USER",
                                               "iat": 1717228800, "exp": 1717315200 }
.
HMACSHA256signature              ← Signature: HMACSHA256(header.payload, secretKey)
```

> ⚠️ **Important:** The payload is Base64 encoded, not encrypted. Anyone can decode and read it. Never include sensitive information like passwords inside a JWT token.

---

## 📖 API Documentation

This project integrates **Swagger UI via SpringDoc OpenAPI 3** for interactive, browser-based API documentation.

### Accessing Swagger UI

Run the application and open:

```
http://localhost:8080/swagger-ui.html
```

### How to Test Protected Endpoints in Swagger

```
Step 1  →  Call POST /api/auth/register to create a user
Step 2  →  Call POST /api/auth/login
Step 3  →  Copy the "accessToken" value from the response body
Step 4  →  Click the green "Authorize 🔓" button at the top right of Swagger
Step 5  →  In the BearerAuth field, paste the token (WITHOUT the word "Bearer")
Step 6  →  Click "Authorize" → then "Close"
Step 7  →  The padlock turns closed 🔒 — you are now authenticated
Step 8  →  Test any protected endpoint directly from the browser
```

---

## ⚙️ Installation & Setup

### Prerequisites

| Tool | Minimum Version | Download |
|---|---|---|
| Java JDK | 17 | [adoptium.net](https://adoptium.net/) |
| Maven | 3.9 | [maven.apache.org](https://maven.apache.org/) |
| PostgreSQL | 15 | [postgresql.org](https://www.postgresql.org/download/) |
| IntelliJ IDEA | Any | [jetbrains.com](https://www.jetbrains.com/idea/) |

---

### Step 1 — Clone the Repository

```bash
git clone https://github.com/Suhail45-LM/notes-management-api.git
cd notes-management-api
```

---

### Step 2 — Set Up the PostgreSQL Database

Open **pgAdmin** or any PostgreSQL terminal and run:

```sql
-- Create the database
CREATE DATABASE notes_db;

-- Create the user
CREATE USER apple WITH PASSWORD 'Password@633';

-- Grant full access
GRANT ALL PRIVILEGES ON DATABASE notes_db TO apple;

-- Connect to notes_db, then grant schema access
\c notes_db
GRANT ALL ON SCHEMA public TO apple;
```

> ✅ Hibernate's `ddl-auto: update` will automatically create the `users` and `notes` tables when the application starts for the first time. No manual SQL table creation is needed.

---

### Step 3 — Configure Environment Variables

#### In IntelliJ IDEA (Recommended)

1. Go to **Run → Edit Configurations**
2. Select **NotesManagementApiApplication**
3. Click **Modify options → Environment Variables**
4. Click the **folder icon 📁** to open the table view
5. Add each variable as a **separate row**:

| Name | Value |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/notes_db` |
| `DB_USERNAME` | `apple` |
| `DB_PASSWORD` | `Password@633` |
| `JWT_SECRET` | `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` |
| `JWT_EXPIRATION` | `86400000` |

6. Click **OK → Apply → OK**

#### macOS / Linux Terminal

```bash
export DB_URL=jdbc:postgresql://localhost:5432/notes_db
export DB_USERNAME=apple
export DB_PASSWORD=Password@633
export JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
export JWT_EXPIRATION=86400000
```

#### Windows CMD

```cmd
set DB_URL=jdbc:postgresql://localhost:5432/notes_db
set DB_USERNAME=apple
set DB_PASSWORD=Password@633
set JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
set JWT_EXPIRATION=86400000
```

---

### Step 4 — Build and Run

```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

### Step 5 — Verify the Application Started

You should see this in the console:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

Started NotesManagementApiApplication in 3.x seconds
```

Then open → **`http://localhost:8080/swagger-ui.html`** ✅

---

## 📋 Example API Usage

### Register

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "suhail_dev",
  "email": "suhail@example.com",
  "password": "Secure@2024"
}
```

```json
{
  "timestamp": "2024-06-01T10:00:00",
  "status": 201,
  "message": "User registered successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "username": "suhail_dev",
    "email": "suhail@example.com",
    "role": "USER",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWhhaWxfZGV2In0.xxxxx",
    "tokenType": "Bearer"
  }
}
```

---

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "suhail_dev",
  "password": "Secure@2024"
}
```

```json
{
  "timestamp": "2024-06-01T10:05:00",
  "status": 200,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWhhaWxfZGV2In0.xxxxx",
    "tokenType": "Bearer"
  }
}
```

---

### Create a Note

```http
POST /api/notes
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "title": "Spring Boot Study Notes",
  "content": "Spring Boot auto-configures components based on classpath dependencies."
}
```

```json
{
  "timestamp": "2024-06-01T10:10:00",
  "status": 201,
  "message": "Note created successfully",
  "data": {
    "id": "abc12345-0000-0000-0000-000000000001",
    "title": "Spring Boot Study Notes",
    "content": "Spring Boot auto-configures components based on classpath dependencies.",
    "createdAt": "2024-06-01T10:10:00",
    "updatedAt": "2024-06-01T10:10:00",
    "owner": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "username": "suhail_dev"
    }
  }
}
```

---

### Validation Error Response

```json
{
  "timestamp": "2024-06-01T10:15:00",
  "status": 400,
  "message": "Validation failed",
  "data": {
    "title": "Title is required",
    "content": "Content is required"
  }
}
```

### Unauthorized Error Response

```json
{
  "timestamp": "2024-06-01T10:20:00",
  "status": 401,
  "message": "Invalid username or password",
  "data": null
}
```

### Forbidden Error Response

```json
{
  "timestamp": "2024-06-01T10:25:00",
  "status": 403,
  "message": "You do not have permission to access this note",
  "data": null
}
```

---

## 🚀 Future Improvements

| Improvement | Description |
|---|---|
| 📄 **Pagination** | Add page-based results for `GET /api/notes` to handle large datasets efficiently |
| 🔍 **Search Notes** | Allow filtering notes by title or content keyword |
| 🔄 **Refresh Tokens** | Issue short-lived access tokens with long-lived refresh tokens for seamless re-authentication |
| 📧 **Email Verification** | Send a confirmation email after registration to verify user identity |
| ☁️ **Cloud Deployment** | Deploy on AWS EC2 with RDS PostgreSQL and HTTPS via ACM |
| 🐳 **Docker Support** | Containerise the application for portable, environment-independent deployment |
| 📦 **Redis Caching** | Cache frequently accessed notes to reduce database load |
| 🧪 **Unit & Integration Tests** | Full JUnit 5 + Mockito test coverage for all service and repository layers |
| 📊 **Rate Limiting** | Protect the login endpoint from brute-force attacks with Bucket4j |
| 🔔 **Activity Notifications** | Email alerts for login from new devices or suspicious activity |

---

## 👤 Author

<div align="center">

### Mohammed Suhail L

**Backend Developer · Java & Spring Boot**

SRM Institute of Science and Technology, Kattankulathur Campus

[![GitHub](https://img.shields.io/badge/GitHub-Suhail45--LM-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Suhail45-LM)

</div>

---

## 📄 License

This project is licensed under the **MIT License**.

```
MIT License

Copyright (c) 2024 Mohammed Suhail L

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<div align="center">

**⭐ If you found this project helpful, consider starring it on GitHub!**

*Built with ❤️ by Mohammed Suhail L — SRM Institute of Science and Technology*

</div>

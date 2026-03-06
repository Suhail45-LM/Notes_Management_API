# 📝 Notes Management API

A **production-ready, enterprise-grade** REST API built with **Java 17 + Spring Boot 3.2** for managing personal notes with stateless JWT authentication and role-based access control.

---

## 🏗 Architecture

```
com.notes.api/
├── config/           # OpenAPI / Swagger configuration
├── controller/       # REST endpoints (thin layer, no business logic)
├── dto/
│   ├── request/      # Validated inbound payloads (records)
│   └── response/     # Outbound DTOs + ApiResponse envelope
├── entity/           # JPA entities (User, Note) + Role enum
├── exception/        # Custom exceptions + GlobalExceptionHandler
├── mapper/           # MapStruct DTO ↔ Entity mappers
├── repository/       # Spring Data JPA repositories
├── security/         # JWT filter, JwtService, SecurityConfig, UserDetailsServiceImpl
├── service/          # Service interfaces
│   └── impl/         # Service implementations
└── util/             # SecurityUtils (current user helper)
```

---

## 🛠 Tech Stack

| Layer         | Technology                      |
|---------------|---------------------------------|
| Language      | Java 17                         |
| Framework     | Spring Boot 3.2.5               |
| Build Tool    | Maven                           |
| Database      | PostgreSQL 15+                  |
| Security      | Spring Security 6 + JWT (JJWT 0.12.5) |
| ORM           | Spring Data JPA (Hibernate 6)   |
| Mapping       | MapStruct 1.5.5                 |
| Validation    | Jakarta Bean Validation 3       |
| Docs          | SpringDoc OpenAPI 3 (Swagger)   |
| Boilerplate   | Lombok                          |

---

## ⚙️ Prerequisites

Make sure the following are installed on your machine:

- **Java 17** — [Download Adoptium](https://adoptium.net/)
- **Maven 3.9+** — [Download Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL 15+** — [Download PostgreSQL](https://www.postgresql.org/download/)
- **IntelliJ IDEA Ultimate** (recommended)

---

## 🗄 Database Setup

### Step 1 — Install PostgreSQL
If not already installed, download and install PostgreSQL for your OS.

### Step 2 — Create the database and user

Open a terminal and run:

```bash
# Connect as the postgres superuser
psql -U postgres

# Create the database
CREATE DATABASE notes_db;

# Create user (matches .env credentials)
CREATE USER apple WITH PASSWORD 'Password@633';

# Grant privileges
GRANT ALL PRIVILEGES ON DATABASE notes_db TO apple;

# Connect to the database and grant schema privileges
\c notes_db
GRANT ALL ON SCHEMA public TO apple;

# Exit
\q
```

### Step 3 — Verify connection

```bash
psql -U apple -d notes_db -h localhost
# If this connects successfully, your DB is ready
```

> **Hibernate DDL**: The application is configured with `ddl-auto: update`.  
> On first startup, Hibernate will automatically create all tables (`users`, `notes`).

---

## 🚀 Running Locally

### Option A — IntelliJ IDEA Ultimate (Recommended)

1. **Clone / copy** the project into your workspace.
2. Open IntelliJ → **File → Open** → select the `notes-api` folder.
3. Wait for Maven to download all dependencies (bottom progress bar).
4. Set environment variables in IntelliJ:
   - Go to **Run → Edit Configurations**
   - Select `NotesManagementApiApplication`
   - Click **Modify options → Environment Variables**
   - Add:
     ```
     DB_URL=jdbc:postgresql://localhost:5432/notes_db
     DB_USERNAME=apple
     DB_PASSWORD=Password@633
     JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
     JWT_EXPIRATION=86400000
     ```
5. Click **Run** (▶).

### Option B — Terminal (Maven)

```bash
# Clone the project
cd notes-api

# Set env vars (Linux/macOS)
export DB_URL=jdbc:postgresql://localhost:5432/notes_db
export DB_USERNAME=apple
export DB_PASSWORD=Password@633
export JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
export JWT_EXPIRATION=86400000

# Build and run
mvn clean install -DskipTests
mvn spring-boot:run
```

### Option C — Windows CMD

```cmd
set DB_URL=jdbc:postgresql://localhost:5432/notes_db
set DB_USERNAME=apple
set DB_PASSWORD=Password@633
set JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
set JWT_EXPIRATION=86400000

mvn spring-boot:run
```

### Verify startup

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v3.2.5)

...
Started NotesManagementApiApplication in 3.2 seconds
```

---

## 📖 API Documentation

Once running, access **Swagger UI** at:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON spec:
```
http://localhost:8080/v3/api-docs
```

---

## 🔐 Authentication Flow

```
1. Register  → POST /api/auth/register  → get JWT token
2. Login     → POST /api/auth/login     → get JWT token
3. Use token → Add header: Authorization: Bearer <token>
```

---

## 🌐 Sample cURL Requests

### 1. Register a new user

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "Secure@123"
  }'
```

**Response:**
```json
{
  "timestamp": "2024-06-01T12:00:00",
  "status": 201,
  "message": "User registered successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "username": "johndoe",
    "email": "john@example.com",
    "role": "USER",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer"
  }
}
```

---

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "Secure@123"
  }'
```

---

### 3. Create a note

```bash
# Replace TOKEN with your actual JWT
curl -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "title": "My First Note",
    "content": "This is the content of my note."
  }'
```

---

### 4. Get all notes

```bash
curl -X GET http://localhost:8080/api/notes \
  -H "Authorization: Bearer TOKEN"
```

---

### 5. Get note by ID

```bash
curl -X GET http://localhost:8080/api/notes/{noteId} \
  -H "Authorization: Bearer TOKEN"
```

---

### 6. Update a note

```bash
curl -X PUT http://localhost:8080/api/notes/{noteId} \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "title": "Updated Title",
    "content": "Updated content here."
  }'
```

---

### 7. Delete a note

```bash
curl -X DELETE http://localhost:8080/api/notes/{noteId} \
  -H "Authorization: Bearer TOKEN"
```

---

## 🔑 JWT Usage

The token is returned in the `accessToken` field on login/register.

```bash
# Save token to variable
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"johndoe","password":"Secure@123"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['data']['accessToken'])")

# Use token
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/notes
```

Token payload contains:
```json
{
  "sub": "johndoe",
  "role": "ROLE_USER",
  "iat": 1717228800,
  "exp": 1717315200
}
```

---

## 👥 Role-Based Access

| Action              | USER | ADMIN |
|---------------------|------|-------|
| Register / Login    | ✅   | ✅    |
| Create note         | ✅   | ✅    |
| View own notes      | ✅   | ✅    |
| View all notes      | ❌   | ✅    |
| Update own note     | ✅   | ❌    |
| Delete own note     | ✅   | ✅    |
| Delete any note     | ❌   | ✅    |

> To create an ADMIN user, manually update the role in the database:
> ```sql
> UPDATE users SET role = 'ADMIN' WHERE username = 'johndoe';
> ```

---

## 📬 Postman Collection

Import the following into Postman:

1. Create a new Collection → **Notes Management API**
2. Set a Collection Variable: `baseUrl = http://localhost:8080`
3. Add requests:

| Name              | Method | URL                            |
|-------------------|--------|--------------------------------|
| Register          | POST   | `{{baseUrl}}/api/auth/register`|
| Login             | POST   | `{{baseUrl}}/api/auth/login`   |
| Create Note       | POST   | `{{baseUrl}}/api/notes`        |
| Get All Notes     | GET    | `{{baseUrl}}/api/notes`        |
| Get Note By ID    | GET    | `{{baseUrl}}/api/notes/{{id}}` |
| Update Note       | PUT    | `{{baseUrl}}/api/notes/{{id}}` |
| Delete Note       | DELETE | `{{baseUrl}}/api/notes/{{id}}` |

For authenticated requests, set **Authorization → Bearer Token → `{{token}}`**

Use a **Post-response script** on Login to auto-save token:
```javascript
const json = pm.response.json();
pm.collectionVariables.set("token", json.data.accessToken);
```

---

## 🧱 Standard API Response Format

All responses follow this envelope:

```json
{
  "timestamp": "2024-06-01T12:00:00",
  "status": 200,
  "message": "Success message",
  "data": {}
}
```

### Error Response

```json
{
  "timestamp": "2024-06-01T12:00:00",
  "status": 404,
  "message": "Note not found with id: '550e8400-...'",
  "data": null
}
```

### Validation Error Response

```json
{
  "timestamp": "2024-06-01T12:00:00",
  "status": 400,
  "message": "Validation failed",
  "data": {
    "title": "Title is required",
    "content": "Content is required"
  }
}
```

---

## 🔒 Security Notes

- Passwords are **BCrypt-hashed** (10 rounds) — never stored in plain text
- JWT tokens are **HS256-signed** with a 256-bit secret key
- Sessions are **completely stateless** — no server-side session storage
- CSRF is disabled (stateless JWT APIs don't need it)
- SQL injection is prevented via **parameterised JPA queries**

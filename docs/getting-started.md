# Getting Started

## Prerequisites

- **Java 17** or later
- **Maven 3.8+** (or use the included Maven wrapper `mvnw` / `mvnw.cmd`)
- **Database** — one of:
    - MySQL 8.x (default profile)
    - PostgreSQL 14+
    - SQLite

## Building the Project

Clone the repository and build all modules:

```bash
# Linux / macOS
./mvnw clean install

# Windows
mvnw.cmd clean install
```

To skip tests during the build:

```bash
./mvnw clean install -DskipTests
```

To build a single module:

```bash
./mvnw clean install -pl parley-api
./mvnw clean install -pl parley-service-jpa
```

## Running the Application

Start the Spring Boot application:

```bash
# Linux / macOS
./mvnw spring-boot:run -pl parley-service-jpa

# Windows
mvnw.cmd spring-boot:run -pl parley-service-jpa
```

The server starts on port **8080** with context path `/parley` and servlet path `/api`.

## Accessing Swagger UI

Once the application is running, open the interactive API documentation:

```
http://localhost:8080/parley/api/swagger-ui/index.html
```

Swagger UI lets you explore all endpoints, view request/response schemas, and execute API calls directly from the browser.

## Creating Your First User

### 1. Sign up

```bash
curl -X POST http://localhost:8080/parley/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "email": "alice@example.com",
    "password": "SecurePass123",
    "confirmPassword": "SecurePass123",
    "timezone": "America/New_York"
  }'
```

The response includes a JWT token:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400000,
  "tokenType": "Bearer"
}
```

### 2. Use the token

Include the token in subsequent requests:

```bash
curl -X GET http://localhost:8080/parley/api/users \
  -H "Authorization: Bearer <your-token>"
```

### 3. Create a chat room

```bash
curl -X POST http://localhost:8080/parley/api/chatrooms \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "General",
    "moderator": { "id": 1 },
    "users": [{ "id": 1 }]
  }'
```

## Running Tests

```bash
./mvnw test -pl parley-service-jpa
```

## Regenerating API Code

After modifying the OpenAPI spec files under `parley-api/src/main/resources/`, regenerate the Spring interfaces and DTOs:

```bash
./mvnw clean compile -pl parley-api
```

Then rebuild the service module to pick up the changes:

```bash
./mvnw clean install -pl parley-service-jpa
```

# Parley

Parley is a Java chat server that emphasizes **OpenAPI code generation** over manual implementation. The API is defined as an OpenAPI 3.0 specification, and Spring controller interfaces and DTOs are generated automatically — keeping the implementation in sync with the spec at all times.

## Technology Stack

| Component | Version |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.8 |
| OpenAPI Generator | 6.6.0 |
| MapStruct | 1.5.5.Final |
| Lombok | via Spring Boot BOM |
| Hibernate | via Spring Boot BOM |
| Databases | MySQL, PostgreSQL, SQLite |

## Features

- **User management** — create users, update profiles, upload avatars
- **Chat rooms** — create, update, delete rooms with custom icons
- **Real-time messaging** — send and retrieve chat messages within rooms
- **Enrollment system** — add/remove users from chat rooms
- **JWT authentication** — stateless security with login, signup, token refresh, and password reset
- **OpenAPI-first development** — all endpoints defined in YAML, code generated automatically
- **Multi-database support** — switch between MySQL, PostgreSQL, and SQLite via Spring profiles

## Documentation

| Section | Description |
|---|---|
| [Getting Started](getting-started.md) | Prerequisites, building, and running the application |
| [Architecture](architecture.md) | Module structure, request flow, and code generation |
| [API Reference](api-reference.md) | All REST endpoints organized by resource |
| [Authentication](authentication.md) | JWT token flow, signup, and password reset |
| [Configuration](configuration.md) | Application properties and deployment settings |
| [Database](database.md) | Entity relationships and supported databases |

## Quick Links

- **Swagger UI**: `http://localhost:8080/parley/api/swagger-ui/index.html`
- **API Docs**: `http://localhost:8080/parley/api/api-docs`
- **Source**: [GitHub](https://github.com/dadgum82/parley)

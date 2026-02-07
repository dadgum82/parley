# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Parley is a Java chat server emphasizing OpenAPI code generation over manual implementation. It uses a multi-module Maven build with Spring Boot 3.2.8, JPA, JWT authentication, MapStruct, and Lombok.

## Build Commands

```bash
# Full build (from project root)
./mvnw clean install

# Build without tests
./mvnw clean install -DskipTests

# Build a single module
./mvnw clean install -pl parley-api
./mvnw clean install -pl parley-service-jpa

# Regenerate API code from OpenAPI spec (required after openapi.yaml changes)
./mvnw clean compile -pl parley-api

# Run the application
./mvnw spring-boot:run -pl parley-service-jpa

# Run tests
./mvnw test -pl parley-service-jpa
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Module Structure

- **parley-api** — OpenAPI 3.0 spec (`src/main/resources/openapi.yaml`) and generated Spring controller interfaces + DTOs. Generated code goes to `target/generated-sources/`. Never edit generated code directly; modify the OpenAPI YAML files instead.
- **parley-service-jpa** — Main Spring Boot application (WAR packaging). Contains all business logic, entities, security, and configuration. Entry point: `ParleyApplication.java`.
- **parley-scripts** — Standalone utilities (e.g., `PasswordResetApplication`).

## Architecture

Request flow: HTTP → `SecurityConfig` → `JwtAuthenticationFilter` → Controller → Service → Repository → Entity

Key layers in `org.sidequest.parley`:
- `controller/` — Implements generated API interfaces from parley-api
- `service/` — Business logic (UserService, ChatRoomService, ChatMessageService, EnrollmentService, AuthenticationService)
- `repository/` — Spring Data JPA repositories
- `entity/` — JPA entities (UserEntity, ChatRoomEntity, ChatMessageEntity, EnrollmentEntity)
- `mapper/` — MapStruct mappers converting between entities and generated API models
- `security/` — JWT filter and token utility
- `config/` — Spring configuration (Security, DB, OpenAPI, Web)

### OpenAPI Code Generation

The API is defined in split YAML files under `parley-api/src/main/resources/`:
- `openapi.yaml` — Root spec with path references
- `openapi/paths/` — Per-resource path definitions (auth, user, chatroom, chatmessage, enrollment)
- `openapi/schemas/` — Per-resource schema definitions
- `openapi/security/` — Security scheme definitions

Path references use JSON Pointer encoding (`~1` for `/`). The openapi-generator-maven-plugin (v6.6.0) generates Spring interfaces in `org.sidequest.parley.api` and models in `org.sidequest.parley.model`.

### Annotation Processing

The maven-compiler-plugin in parley-service-jpa is configured with annotation processor ordering: MapStruct → Lombok → lombok-mapstruct-binding. This order matters for correct code generation.

### Authentication

JWT-based stateless auth. Public endpoints (no token required): `/auth/login`, `/auth/signup`, `/auth/password/reset`, `/swagger-ui/**`, `/api-docs/**`. All other endpoints require a Bearer token in the Authorization header.

### Database

Active profile is `mysql` by default. Profile-specific configs in `application-{profile}.properties`. Supports MySQL (primary), PostgreSQL, and SQLite. Hibernate `ddl-auto=update` manages schema. All timestamps stored in UTC; `TimeHelper` converts for display using user timezone.

### File Storage

User avatars and chatroom icons are stored on the local filesystem at paths configured in `application.properties` (`user.avatar.directory`, `chatroom.icon.directory`). `FileSystemHelper` handles read/write operations.

## Key Conventions

- Controllers implement interfaces generated from OpenAPI — add new endpoints by modifying the YAML spec, regenerating, then implementing the new interface methods.
- MapStruct mappers handle all entity ↔ DTO conversion. When adding fields, update both the entity and the corresponding mapper.
- Lombok `@Data`, `@Builder`, etc. are used on entities — avoid writing manual getters/setters.
- `@Lazy` is used in some service constructors to break circular dependencies (`spring.main.allow-circular-references=true` is enabled).
- Swagger UI available at `http://localhost:8080/parley/api/swagger-ui/index.html`.

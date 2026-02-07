# Architecture

## Module Structure

Parley uses a multi-module Maven build with three modules:

```
parley/
├── parley-api/              # OpenAPI spec + generated code
├── parley-service-jpa/      # Spring Boot application
└── parley-scripts/          # Standalone utilities
```

### parley-api

Contains the OpenAPI 3.0 specification and generates Spring controller interfaces and DTO classes. Generated code goes to `target/generated-sources/` and should never be edited directly.

The OpenAPI spec is split across multiple YAML files:

```
src/main/resources/
├── openapi.yaml                    # Root spec with path references
└── openapi/
    ├── paths/
    │   ├── auth.yaml               # Authentication endpoints
    │   ├── user.yaml               # User endpoints
    │   ├── chatroom.yaml           # Chat room endpoints
    │   ├── chatmessage.yaml        # Chat message endpoints
    │   └── enrollment.yaml         # Enrollment endpoints
    ├── schemas/
    │   ├── auth.yaml               # Auth DTOs
    │   ├── user.yaml               # User DTOs
    │   ├── chatroom.yaml           # Chat room DTOs
    │   ├── chatmessage.yaml        # Chat message DTOs
    │   ├── enrollment.yaml         # Enrollment DTOs
    │   └── common.yaml             # Shared types (Error)
    └── security/
        └── security.yaml           # Security scheme definitions
```

Path references in the root spec use JSON Pointer encoding (`~1` for `/`). The `openapi-generator-maven-plugin` (v6.6.0) generates:

- **Interfaces** in `org.sidequest.parley.api` — one per resource tag
- **Models** in `org.sidequest.parley.model` — request/response DTOs

### parley-service-jpa

The main Spring Boot application (WAR packaging). Contains all business logic, JPA entities, security, and configuration. Entry point: `ParleyApplication.java`.

### parley-scripts

Standalone utilities such as `PasswordResetApplication` for administrative tasks outside the running server.

## Request Flow

```
HTTP Request
    │
    ▼
SecurityConfig          ← CORS, CSRF, public endpoint rules
    │
    ▼
JwtAuthenticationFilter ← Extract & validate Bearer token
    │
    ▼
Controller              ← Implements generated API interface
    │
    ▼
Service                 ← Business logic & validation
    │
    ▼
Repository              ← Spring Data JPA queries
    │
    ▼
Entity                  ← JPA entity / database row
```

## Package Layout

All application code lives under `org.sidequest.parley` in the `parley-service-jpa` module:

| Package | Purpose |
|---|---|
| `controller/` | Implements generated API interfaces from parley-api |
| `service/` | Business logic — `UserService`, `ChatRoomService`, `ChatMessageService`, `EnrollmentService`, `AuthenticationService` |
| `repository/` | Spring Data JPA repositories |
| `entity/` | JPA entities — `UserEntity`, `ChatRoomEntity`, `ChatMessageEntity`, `EnrollmentEntity` |
| `mapper/` | MapStruct mappers for entity-to-DTO conversion |
| `security/` | `JwtAuthenticationFilter`, `JwtTokenUtil` |
| `config/` | `SecurityConfig`, `DbConfig`, `OpenAPIConfig`, `WebConfig` |
| `util/` | Helpers — `TimeHelper`, `FileSystemHelper`, `EmailHelper` |
| `exception/` | Custom exceptions — `ValidationException`, `DuplicateResourceException` |
| `handler/` | Global exception handlers — `ValidationExceptionHandler` |

## OpenAPI Code Generation Workflow

To add or modify an API endpoint:

1. Edit the YAML files under `parley-api/src/main/resources/openapi/`
2. Regenerate code: `mvnw clean compile -pl parley-api`
3. Implement the new/changed interface method in the corresponding controller
4. Add any new MapStruct mapper methods if new DTOs were introduced
5. Rebuild: `mvnw clean install -pl parley-service-jpa`

## MapStruct Mapping Layer

MapStruct mappers convert between JPA entities and generated API models. Each mapper is a singleton accessed via `INSTANCE`:

- `UserMapper` — `UserEntity` ↔ `BasicUser` / `User`
- `ChatRoomMapper` — `ChatRoomEntity` ↔ `ChatRoom`
- `ChatMessageMapper` — `ChatMessageEntity` ↔ `ChatMessage`
- `EnrollmentMapper` — `EnrollmentEntity` ↔ `Enrollment`

The annotation processor order in the Maven compiler plugin is: **MapStruct → Lombok → lombok-mapstruct-binding**. This order is required for MapStruct to correctly use Lombok-generated getters/setters.

## Dependency Injection Notes

Some services have circular dependencies (e.g., `ChatRoomService` ↔ `ChatMessageService`). These are resolved using `@Lazy` on injected dependencies, with `spring.main.allow-circular-references=true` enabled in application properties.

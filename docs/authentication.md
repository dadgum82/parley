# Authentication & Security

Parley uses **JWT (JSON Web Token)** stateless authentication. Tokens are issued at login or signup and must be included in every request to protected endpoints.

## JWT Token Flow

```
1. Client sends credentials
       │
       ▼
   POST /auth/login  ──or──  POST /auth/signup
       │
       ▼
2. Server validates credentials
       │
       ▼
3. Server returns JWT token
       │
       ▼
4. Client stores token
       │
       ▼
5. Client sends token in every request:
   Authorization: Bearer <token>
       │
       ▼
6. JwtAuthenticationFilter validates token
       │
       ▼
7. Request proceeds to controller
```

## Login

Send a `POST` to `/auth/login` with username and password:

```bash
curl -X POST http://localhost:8080/parley/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "password": "SecurePass123"}'
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400000,
  "tokenType": "Bearer"
}
```

Set `rememberMe: true` in the request body to extend the token expiration to 7 days (instead of the default 24 hours).

## Signup

Send a `POST` to `/auth/signup` to create a new account and receive a token in one step:

```bash
curl -X POST http://localhost:8080/parley/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "bob",
    "email": "bob@example.com",
    "password": "SecurePass456",
    "confirmPassword": "SecurePass456",
    "timezone": "America/Chicago"
  }'
```

Validation rules:

- Username must be unique
- Email must be unique and valid
- Password and confirmPassword must match
- Timezone is optional (defaults to server timezone)

## Token Refresh

To get a new token before the current one expires, send a `POST` to `/auth/refresh` with your current valid token:

```bash
curl -X POST http://localhost:8080/parley/api/auth/refresh \
  -H "Authorization: Bearer <current-token>"
```

## Password Change

Authenticated users can change their password via `PUT /auth/password`:

```bash
curl -X PUT http://localhost:8080/parley/api/auth/password \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "OldPass123",
    "newPassword": "NewPass456",
    "confirmPassword": "NewPass456"
  }'
```

## Password Reset

For users who have forgotten their password:

1. **Request a reset** — `POST /auth/password/reset` with the account email. A reset link is sent via email.
2. **Reset the password** — `POST /auth/password/reset/{token}` with the new password and confirmation.

```bash
# Step 1: Request reset
curl -X POST http://localhost:8080/parley/api/auth/password/reset \
  -H "Content-Type: application/json" \
  -d '{"email": "alice@example.com"}'

# Step 2: Use token from email link
curl -X POST http://localhost:8080/parley/api/auth/password/reset/abc123-reset-token \
  -H "Content-Type: application/json" \
  -d '{"password": "NewPass789", "confirmPassword": "NewPass789"}'
```

Reset tokens expire after 24 hours.

## Public vs Protected Endpoints

### Public (no token required)

| Endpoint | Purpose |
|---|---|
| `POST /auth/login` | User login |
| `POST /auth/signup` | User registration |
| `POST /auth/password/reset` | Request password reset |
| `GET /swagger-ui/**` | Swagger UI |
| `GET /api-docs/**` | OpenAPI spec |
| `GET /v3/api-docs/**` | OpenAPI v3 spec |

### Protected (Bearer token required)

All other endpoints require a valid JWT token. Requests without a token or with an expired/invalid token receive a `401 Unauthorized` response.

## Security Configuration Details

The security layer is implemented in `SecurityConfig.java`:

- **CSRF** is disabled (stateless API)
- **CORS** is enabled for all origins (configured in `WebConfig`)
- **Session management** is stateless — no server-side sessions
- **Password encoding** uses BCrypt via `BCryptPasswordEncoder`
- **Filter chain** — `JwtAuthenticationFilter` runs before Spring's `UsernamePasswordAuthenticationFilter`

### JwtAuthenticationFilter

The filter intercepts every request and:

1. Extracts the `Bearer` token from the `Authorization` header
2. Validates the token signature and expiration using HMAC-SHA256
3. Loads the user details from the database
4. Sets the authentication context in `SecurityContextHolder`

The filter skips processing for public paths (`/swagger-ui`, `/api-docs`, `/auth/login`, `/auth/signup`, `/auth/password/reset`).

### Token Structure

Tokens are signed with HMAC-SHA256 using a secret key configured in `application.properties` (`jwt.secret`). The token payload contains:

- **Subject** — the username
- **Issued at** — timestamp
- **Expiration** — configurable via `jwt.expiration` (default: 86400000 ms = 24 hours)

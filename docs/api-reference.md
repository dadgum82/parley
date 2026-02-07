# API Reference

All endpoints are served under the base path `/parley/api`. Unless noted as public, every endpoint requires a `Bearer` token in the `Authorization` header.

For interactive exploration, see the [Swagger UI](http://localhost:8080/parley/api/swagger-ui/index.html).

---

## Authentication

Public endpoints for user registration, login, and password management.

### POST /auth/login

Authenticate a user and receive a JWT token.

**Public** — no token required.

| Field | Type | Description |
|---|---|---|
| `username` | string | Username |
| `password` | string | Password |
| `rememberMe` | boolean | Extend token expiration to 7 days |

**Response** `200`: `AuthResponse` — `token`, `expiresIn`, `tokenType`

### POST /auth/signup

Register a new user account.

**Public** — no token required.

| Field | Type | Description |
|---|---|---|
| `username` | string | Desired username |
| `email` | string | Email address |
| `password` | string | Password |
| `confirmPassword` | string | Must match password |
| `timezone` | string | IANA timezone (optional) |

**Response** `201`: `AuthResponse`

### POST /auth/refresh

Get a new JWT token using a valid existing token.

**Response** `200`: `AuthResponse`

### POST /auth/logout

Invalidate the current JWT token.

**Response** `200`

### PUT /auth/password

Change the authenticated user's password.

| Field | Type | Description |
|---|---|---|
| `currentPassword` | string | Current password |
| `newPassword` | string | New password |
| `confirmPassword` | string | Must match new password |

**Response** `200`

### POST /auth/password/reset

Request a password reset email.

**Public** — no token required.

| Field | Type | Description |
|---|---|---|
| `email` | string | Email associated with the account |

**Response** `200`

### POST /auth/password/reset/{token}

Reset password using the token from the email link.

| Field | Type | Description |
|---|---|---|
| `password` | string | New password |
| `confirmPassword` | string | Must match password |

**Response** `200`

---

## Users

Manage user accounts and profiles.

### GET /users

Get a list of all users.

**Response** `200`: Array of `BasicUser`

### POST /users

Create a new user.

| Field | Type | Description |
|---|---|---|
| `name` | string | Username |
| `timezone` | string | IANA timezone |
| `email` | string | Email address |

**Response** `201`: `BasicUser`

### GET /users/{id}

Get a user by ID.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |

**Response** `200`: `BasicUser`

### PUT /users/{id}

Update an existing user.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |

**Body**: `NewUser` — `name`, `timezone`, `email`

**Response** `200`: `BasicUser`

### GET /users/{id}/avatar

Get a user's avatar image.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |

**Response** `200`: Binary image (JPEG/PNG)

### POST /users/{id}/avatar

Upload a user's avatar image.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |
| `file` | file | multipart/form-data |

**Response** `200`

### GET /users/{userId}/chatrooms

Get all chat rooms a user is enrolled in.

| Parameter | Type | In |
|---|---|---|
| `userId` | integer (int64) | path |

**Response** `200`: Array of `ChatRoom`

---

## Chat Rooms

Create and manage chat rooms.

### GET /chatrooms

Get a list of all chat rooms.

**Response** `200`: Array of `ChatRoom`

### POST /chatrooms

Create a new chat room.

| Field | Type | Description |
|---|---|---|
| `name` | string | Room name |
| `moderator` | BasicUser | User who moderates the room |
| `users` | BasicUser[] | Initial members |

**Response** `201`: `ChatRoom`

### GET /chatrooms/{id}

Get a chat room by ID.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |

**Response** `200`: `ChatRoom`

### PUT /chatrooms/{id}

Update a chat room.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |

**Body**: `ChatRoom`

**Response** `200`: `ChatRoom`

### DELETE /chatrooms/{id}

Delete a chat room.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |

**Response** `204`

### GET /chatrooms/{id}/icon

Get a chat room's icon image.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |

**Response** `200`: Binary image (JPEG/PNG)

### POST /chatrooms/{id}/icon

Upload a chat room icon.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |
| `file` | file | multipart/form-data |

**Response** `200`

### GET /chatrooms/{chatRoomId}/chats

Get all chat messages in a chat room.

| Parameter | Type | In |
|---|---|---|
| `chatRoomId` | integer (int64) | path |

**Response** `200`: Array of `ChatMessage`

---

## Chat Messages

Send and retrieve individual messages.

### POST /chats

Create a new chat message.

| Field | Type | Description |
|---|---|---|
| `chatRoomId` | integer (int64) | Target chat room |
| `userId` | integer (int64) | Sending user |
| `content` | string | Message text |
| `screenEffect` | string | Screen effect (optional) |
| `textEffect` | string | Text effect (optional) |

The user must be enrolled in the chat room to send a message.

**Response** `201`: `ChatMessage`

### GET /chats/{id}

Get a single chat message by ID.

| Parameter | Type | In |
|---|---|---|
| `id` | integer (int64) | path |

**Response** `200`: `ChatMessage`

---

## Enrollments

Manage chat room membership. Enrollments link users to chat rooms.

### POST /enrollments

Add multiple users to chat rooms (bulk operation).

**Body**: Array of `NewEnrollment`

| Field | Type | Description |
|---|---|---|
| `userId` | integer (int64) | User to enroll |
| `chatRoomId` | integer (int64) | Target chat room |

**Response** `201`: Array of `Enrollment`

### GET /enrollments/{chatRoomId}

Get all enrollments for a chat room.

| Parameter | Type | In |
|---|---|---|
| `chatRoomId` | integer (int64) | path |

**Response** `200`: Array of `Enrollment`

### DELETE /enrollments/{chatRoomId}

Remove all users from a chat room.

| Parameter | Type | In |
|---|---|---|
| `chatRoomId` | integer (int64) | path |

**Response** `204`

### POST /enrollments/{chatRoomId}/users/{userId}

Add a single user to a chat room.

| Parameter | Type | In |
|---|---|---|
| `chatRoomId` | integer (int64) | path |
| `userId` | integer (int64) | path |

**Response** `201`

### DELETE /enrollments/{chatRoomId}/users/{userId}

Remove a single user from a chat room.

| Parameter | Type | In |
|---|---|---|
| `chatRoomId` | integer (int64) | path |
| `userId` | integer (int64) | path |

**Response** `204`

---

## Error Responses

All endpoints may return error responses with the following structure:

```json
{
  "code": "string",
  "message": "string",
  "fields": "string"
}
```

Common HTTP status codes:

| Code | Meaning |
|---|---|
| `400` | Bad request / validation error |
| `401` | Unauthorized — missing or invalid token |
| `404` | Resource not found |
| `405` | Method not allowed |
| `500` | Internal server error |

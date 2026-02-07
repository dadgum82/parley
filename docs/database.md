# Database Schema

Parley uses JPA entities with Hibernate `ddl-auto=update` to manage the database schema automatically. All timestamps are stored in UTC; the `TimeHelper` utility converts to the user's timezone for display.

## Entity Relationship Overview

```
┌──────────────┐       ┌─────────────────────┐       ┌──────────────────┐
│  chatusers   │       │ chatrooms_chatusers  │       │    chatrooms     │
│              │       │   (enrollments)      │       │                  │
│  id (PK)     │◄──────│  chatuser_id (FK)    │       │  id (PK)         │
│  name        │       │  chatroom_id (FK)    │──────►│  name            │
│  magic       │       │  id (PK)             │       │  icon_path       │
│  email       │       └─────────────────────┘       │  moderator_id(FK)│──┐
│  avatar_path │                                      └──────────────────┘  │
│  timezone    │                                                            │
│  ...         │◄───────────────────────────────────────────────────────────┘
└──────┬───────┘
       │
       │         ┌──────────────────┐
       │         │   chatmessages   │
       │         │                  │
       └────────►│  user_id (FK)    │
                 │  chatroom_id(FK) │──────► chatrooms
                 │  id (PK)         │
                 │  content         │
                 │  timestamp       │
                 │  screen_effect   │
                 │  text_effect     │
                 └──────────────────┘
```

## Entities

### UserEntity

**Table:** `chatusers`

| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT (PK) | Auto-generated |
| `name` | VARCHAR | Username (unique) |
| `magic` | VARCHAR | BCrypt password hash |
| `email` | VARCHAR | Unique |
| `avatar_path` | VARCHAR | Filesystem path to avatar image |
| `timezone` | VARCHAR | IANA timezone (e.g., `America/New_York`) |
| `created_at` | TIMESTAMP | Account creation time (UTC) |
| `updated_at` | TIMESTAMP | Last update time (UTC) |
| `last_posted_message_date_time` | TIMESTAMP | Last message timestamp (UTC) |
| `password_reset_token` | VARCHAR | Token for password reset flow |
| `password_reset_token_expiration` | TIMESTAMP | Token expiry (UTC) |

Implements Spring Security's `UserDetails` interface. Has a one-to-many relationship with `EnrollmentEntity`.

### ChatRoomEntity

**Table:** `chatrooms`

| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT (PK) | Auto-generated |
| `name` | VARCHAR | Room name |
| `icon_path` | VARCHAR | Filesystem path to icon image |
| `moderator_id` | BIGINT (FK) | References `chatusers.id` |

Relationships:

- **One-to-one** with `UserEntity` (moderator)
- **One-to-many** with `EnrollmentEntity` (room memberships)
- **Many-to-one** with `ChatMessageEntity` (latest message, lazy-loaded)

### ChatMessageEntity

**Table:** `chatmessages`

| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT (PK) | Auto-generated |
| `chatroom_id` | BIGINT (FK) | References `chatrooms.id` (non-null) |
| `user_id` | BIGINT (FK) | References `chatusers.id` (non-null) |
| `content` | VARCHAR | Message text |
| `timestamp` | TIMESTAMP | When the message was sent (UTC) |
| `screen_effect` | VARCHAR | Optional screen effect identifier |
| `text_effect` | VARCHAR | Optional text effect identifier |

Relationships:

- **Many-to-one** with `ChatRoomEntity` (the room this message belongs to)
- **Many-to-one** with `UserEntity` (the message sender)

### EnrollmentEntity

**Table:** `chatrooms_chatusers`

This is the junction table implementing the many-to-many relationship between users and chat rooms.

| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT (PK) | Auto-generated |
| `chatroom_id` | BIGINT (FK) | References `chatrooms.id` |
| `chatuser_id` | BIGINT (FK) | References `chatusers.id` |

Relationships:

- **Many-to-one** with `ChatRoomEntity`
- **Many-to-one** with `UserEntity`

## Relationships Summary

| Relationship | Type | Description |
|---|---|---|
| User ↔ ChatRoom | Many-to-many | Via `chatrooms_chatusers` (enrollments) |
| ChatRoom → User | One-to-one | Moderator |
| ChatMessage → ChatRoom | Many-to-one | Each message belongs to one room |
| ChatMessage → User | Many-to-one | Each message has one sender |

## Supported Databases

| Database | Profile | Driver | Dialect |
|---|---|---|---|
| MySQL 8.x | `mysql` | `com.mysql.cj.jdbc.Driver` | `MySQL8Dialect` |
| PostgreSQL | `postgres` | (auto-detected) | `PostgreSQLDialect` |
| SQLite | `sqlite` | `org.sqlite.JDBC` | `SQLiteDialect` |

### Switching Databases

Set the profile in `application.properties`:

```properties
spring.profiles.active=postgres
```

Or via command line:

```bash
./mvnw spring-boot:run -pl parley-service-jpa -Dspring-boot.run.profiles=sqlite
```

Hibernate's `ddl-auto=update` creates or updates tables automatically when the application starts. No manual migration scripts are needed for development.

## Repositories

Spring Data JPA repositories provide data access:

| Repository | Entity | Notable Methods |
|---|---|---|
| `UserRepository` | `UserEntity` | `findByName`, `findByEmail`, `updatePasswordResetToken`, `updatePassword` |
| `ChatRoomRepository` | `ChatRoomEntity` | Standard CRUD |
| `ChatMessageRepository` | `ChatMessageEntity` | `findByChatRoomId` |
| `EnrollmentRepository` | `EnrollmentEntity` | `findByChatroomId`, `findByChatroomIdAndChatuserId`, `existsByChatroomIdAndChatuserId` |

# Configuration

## Application Properties

The main configuration file is located at:

```
parley-service-jpa/src/main/resources/application.properties
```

### Server Settings

| Property | Default | Description |
|---|---|---|
| `server.port` | `8080` | HTTP port |
| `server.servlet.context-path` | `/parley` | Application context path |
| `spring.mvc.servlet.path` | `/api` | Servlet path (all endpoints are under `/parley/api/`) |

### Database

| Property | Description |
|---|---|
| `spring.profiles.active` | Active database profile (`mysql`, `postgres`, or `sqlite`) |
| `spring.datasource.url` | JDBC connection URL |
| `spring.datasource.driver-class-name` | JDBC driver class |
| `spring.datasource.username` | Database username |
| `spring.datasource.password` | Database password |
| `spring.jpa.hibernate.ddl-auto` | Schema management (`update` by default) |
| `spring.jpa.properties.hibernate.dialect` | Hibernate dialect for the target database |
| `spring.jpa.show-sql` | Log SQL statements (`true` / `false`) |

### JWT Authentication

| Property | Default | Description |
|---|---|---|
| `jwt.secret` | (configured) | HMAC-SHA256 signing key (Base64-encoded) |
| `jwt.expiration` | `86400000` | Token expiration in milliseconds (24 hours) |

### File Storage

| Property | Description |
|---|---|
| `user.avatar.directory` | Filesystem path for user avatar images |
| `chatroom.icon.directory` | Filesystem path for chat room icon images |
| `app.fileDirectory` | General file upload directory |

### Email (Password Reset)

| Property | Description |
|---|---|
| `spring.mail.host` | SMTP server hostname |
| `spring.mail.port` | SMTP server port |
| `spring.mail.username` | SMTP username |
| `spring.mail.password` | SMTP password |
| `spring.mail.from` | Sender email address |

### Swagger / OpenAPI

| Property | Default | Description |
|---|---|---|
| `springdoc.api-docs.path` | `/api-docs` | Path for the OpenAPI JSON spec |
| `springdoc.swagger-ui.path` | `/swagger-ui.html` | Swagger UI path |
| `springdoc.swagger-ui.try-it-out-enabled` | `true` | Enable "Try it out" in Swagger UI |

### Other

| Property | Default | Description |
|---|---|---|
| `default.timezone` | `America/New_York` | Default timezone for date/time display |
| `spring.main.allow-circular-references` | `true` | Required for `@Lazy` circular dependency resolution |

## Database Profiles

Parley supports three databases via Spring profiles. Each profile has its own properties file.

### MySQL (default)

**File:** `application-mysql.properties`

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/parley?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### PostgreSQL

**File:** `application-postgres.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parley
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### SQLite

**File:** `application-sqlite.properties`

```properties
spring.jpa.database-platform=org.hibernate.dialect.SQLiteDialect
spring.datasource.url=jdbc:sqlite:/path/to/parley.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

### Switching Profiles

Set the active profile in `application.properties`:

```properties
spring.profiles.active=postgres
```

Or pass it as a command-line argument:

```bash
./mvnw spring-boot:run -pl parley-service-jpa -Dspring-boot.run.profiles=postgres
```

## Logging

Logging is configured in `application.properties`:

| Property | Default | Description |
|---|---|---|
| `logging.level.root` | `DEBUG` | Root log level |
| `logging.level.org.sidequest.parley` | `DEBUG` | Application log level |
| `logging.level.org.sidequest.parley.security` | `DEBUG` | Security/JWT log level |
| `logging.file.path` | (configured) | Directory for log files |
| `logging.file.name` | `parley.log` | Log file name |

## Tomcat Deployment

For deploying the WAR file to an external Tomcat server:

### catalina.properties

Set the active Spring profile:

```properties
spring.profiles.active=mysql
```

### server.xml

Configure a JNDI DataSource in the `GlobalNamingResources` section:

```xml
<Resource name="jdbc/parleyDB"
          global="jdbc/parleyDB"
          auth="Container"
          type="javax.sql.DataSource"
          driverClassName="com.mysql.cj.jdbc.Driver"
          url="jdbc:mysql://localhost:3306/parley"
          maxActive="100"
          maxIdle="20"
          minIdle="5"
          maxWait="10000"/>
```

### context.xml

Link the JNDI resource so the application can look it up:

```xml
<ResourceLink name="jdbc/parleyDB"
              global="jdbc/parleyDB"
              auth="Container"
              type="javax.sql.DataSource" />
```

Make sure the database JDBC driver JAR is present in Tomcat's `lib/` directory.

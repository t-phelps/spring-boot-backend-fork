## Spring Boot Backend Fork

Spring Boot backend starter with JWT cookie auth, Spring Security, jOOQ, Flyway, and PostgreSQL. Fork this to skip the boilerplate on new projects.

> **Environment variables** are managed with [direnv](https://direnv.net/). Install it, add `eval "$(direnv hook bash)"` to your `~/.bashrc`, then run `direnv allow` in this directory. Variables in `.env` will be automatically loaded when you `cd` into the project and unloaded when you leave — no conflicts with system environment variables.

---

### Prerequisites

- Java 17+
- Maven 3.9+ (or use the included `mvnw` wrapper)
- Docker & Docker Compose (for local PostgreSQL)
- [direnv](https://direnv.net/) (for automatic `.env` loading)

---

### Quick Start

**1. Copy and fill in environment variables**
```bash
cp .env.example .env
# Edit .env with your values
```

**2. Start PostgreSQL**
```bash
docker-compose up -d
```

**3. Run Flyway migrations**
```bash
cd backend
./mvnw flyway:migrate
```

**4. Generate jOOQ sources** (requires a running database with migrations applied; only needed after schema changes)
```bash
./mvnw generate-sources -Pgenerate-jooq
```

**5. Run the application**
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The API will be available at `http://localhost:8080`.
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

### Environment Variables

| Variable | Required | Default | Description |
|---|---|---|---|
| `DB_HOST` | No | `localhost` | PostgreSQL host |
| `DB_PORT` | No | `5432` | PostgreSQL port |
| `DB_NAME` | **Yes** | — | Database name |
| `DB_USERNAME` | **Yes** | — | Database user |
| `DB_PASSWORD` | **Yes** | — | Database password |
| `JWT_SECRET` | **Yes** | — | JWT signing secret (min 32 chars). Generate with: `openssl rand -hex 32` |
| `JWT_EXPIRATION_MS` | No | `3600000` | Token lifetime in milliseconds (default 1 hour) |
| `FRONTEND_URL` | No | `http://localhost:3000` | Allowed CORS origin |
| `COOKIE_SECURE` | No | `true` | Set to `false` when running locally without HTTPS |

---

### Adding New Endpoints

1. Create your controller, service, and repository classes.
2. If needed, add new tables via a Flyway migration in `src/main/resources/db/migration/` (e.g. `V2__create_posts_table.sql`).
3. Re-run `./mvnw flyway:migrate` then `./mvnw generate-sources` to regenerate jOOQ classes.
4. Protect the endpoint in `SecurityConfig.java` by adding a `.requestMatchers(...)` rule before the `.anyRequest().authenticated()` line.

---

### Project Structure

```
backend/src/main/java/com/tphelps/backend/
├── config/          # Spring Security, JWT filter, auth provider
├── controller/      # REST controllers (/auth, /account)
├── dtos/            # Request/response records with validation annotations
├── exception/       # Global exception handler
├── jwt/             # JWT generation and validation
├── repository/      # jOOQ database access
└── service/         # Business logic

backend/src/main/resources/
├── application.properties       # Main config (reads from environment variables)
├── application-dev.properties   # Dev profile overrides (cookie.secure=false)
└── db/migration/                # Flyway SQL migrations
```

---

### Changes Required After Forking

1. Set all required environment variables in `.env` (copy from `.env.example`).
2. Update `spring.application.name` in `application.properties`.
3. If not using PostgreSQL, swap the driver dependency in `pom.xml` and update the jOOQ database dialect.
4. Update the `groupId` and package names (`com.tphelps.backend` → your package) throughout `pom.xml` and all Java files.

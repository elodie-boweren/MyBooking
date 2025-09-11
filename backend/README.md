# MyBooking Backend

Spring Boot backend for MyBooking. This repo contains the feature-based package structure, Flyway migrations folder, and configuration to run locally with either Dockerized Postgres or a local Postgres install.

## Run locally (single dev profile)

You can use either option below. Both use the same `application.yml` and `dev` profile; DB connection is injected via env vars.

### Option A — Docker (Postgres in a container)
```bash
# 1) Start DB
docker compose up -d

# 2) Run the app
export DB_USER=mybooking DB_PASS=mybooking
./mvnw -Dspring-boot.run.profiles=dev spring-boot:run
```

### Option B — Local Postgres (no Docker)
```bash
# 1) Create role and database (run once)
psql -U postgres -c "CREATE ROLE mybooking WITH LOGIN PASSWORD 'mybooking';"
psql -U postgres -c "CREATE DATABASE mybooking OWNER mybooking;"

# 2) Run the app
export DB_USER=mybooking DB_PASS=mybooking
./mvnw -Dspring-boot.run.profiles=dev spring-boot:run
```

### Overrides
```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://127.0.0.1:5433/otherdb"
export DB_USER=otheruser DB_PASS=otherpass
./mvnw -Dspring-boot.run.profiles=dev spring-boot:run
```

## Tech stack
- Spring Boot 3.5 (Web, Security, Validation, Data JPA)
- PostgreSQL, Flyway (migrations)
- MapStruct (DTO mapping)
- springdoc-openapi (Swagger UI)
- Testcontainers (integration tests)

## Package layout
See `src/main/java/com/MyBooking/**/README.md` in each feature package for responsibilities, endpoints, entities, and TODO checklists.

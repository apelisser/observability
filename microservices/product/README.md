# Product Service

`product-service` manages product reservations. It exposes HTTP endpoints to create, confirm, and cancel reservations, storing reservation state in an H2 in-memory database.

## Stack

- Spring Boot `3.5.13`
- Java `17`
- Maven Wrapper
- Spring Web
- Spring Data JPA
- Bean Validation
- H2
- Lombok
- OpenTelemetry API

## Observability Strategy

This service primarily represents the OpenTelemetry Java Agent strategy.

The application is instrumented at runtime by the agent:

- The `Dockerfile` downloads `opentelemetry-javaagent.jar`.
- Docker Compose injects `JAVA_TOOL_OPTIONS=-javaagent:/opt/otel/opentelemetry-javaagent.jar`.
- `OTEL_*` variables configure service name, protocol, and exporters.
- HTTP, JDBC, logs, and metrics are collected according to the Java Agent's supported instrumentation.

The service also includes a small application-level extension:

- `opentelemetry-api`
- `TraceIdFilter`, which reads the active span and adds `X-Trace-Id` to HTTP responses.

This keeps the main instrumentation model zero-code while still allowing targeted application behavior where the POC needs it.

## Runtime Configuration

Default port:

```text
8081
```

Important properties:

```text
spring.application.name=product
spring.datasource.url=jdbc:h2:mem:product;DB_CLOSE_DELAY=-1
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.h2.console.path=/h2-console
```

## Endpoints

Create reservation:

```http
POST /products/{productId}/reservations
Content-Type: application/json
```

Example:

```json
{
  "quantity": 2
}
```

Confirm reservation:

```http
PATCH /products/{productId}/reservations/{reservationId}/confirm
```

Cancel reservation:

```http
PATCH /products/{productId}/reservations/{reservationId}/cancel
```

## Functional Flow

1. Create a reservation with status `PENDING`.
2. Return `reservationId`, `productId`, and reserved quantity.
3. Later update the reservation status to `CONFIRMED` or `CANCELLED`.

## Integrations

- Called by `order-service` during checkout.
- H2 in-memory database.
- OTLP export through the Java Agent when agent-based instrumentation is enabled.

## Run Locally

### 1. Run without the Java Agent:

```bash
./mvnw spring-boot:run
```

### 2. Run locally with Grafana LGTM and the OpenTelemetry Java Agent:

Start Grafana LGTM first. The agent exports telemetry to the OTLP HTTP endpoint exposed on `localhost:4318`.

```bash
docker compose up -d grafana-lgtm
```

Then start the service with the Java Agent:

```bash
../../scripts/download-otel-agent.sh

JAVA_TOOL_OPTIONS="-javaagent:../../otel/agent/opentelemetry-javaagent.jar" \
OTEL_SERVICE_NAME=product-service \
OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318 \
OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf \
OTEL_TRACES_EXPORTER=otlp \
OTEL_METRICS_EXPORTER=otlp \
OTEL_LOGS_EXPORTER=otlp \
./mvnw spring-boot:run
```

This command assumes it is executed from `microservices/product`. The script stores the agent under the repository root at `otel/agent/opentelemetry-javaagent.jar`.

If `grafana-lgtm` is not running, the application may still start, but the Java Agent will not be able to export telemetry to `http://localhost:4318`.

When finished, stop Grafana LGTM:

```bash
docker compose down
```

### 3. Run through Compose with Grafana LGTM and the Java Agent:

```bash
docker compose up -d --build grafana-lgtm product-service
```

In this mode, Docker Compose builds `product-service`, injects the Java Agent through `JAVA_TOOL_OPTIONS`, and configures OTLP export to `http://grafana-lgtm:4318` inside the Compose network.

When finished, stop the Compose stack:

```bash
docker compose down
```

## POC Analysis

- Lowest application intrusion for broad instrumentation.
- Good candidate for operational standardization across services.
- Less fine-grained by default than explicit Micrometer or Spring Boot native instrumentation.
- The added OpenTelemetry API dependency is a deliberate exception for exposing the trace ID at the HTTP boundary.

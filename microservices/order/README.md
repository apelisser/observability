# Order Service

`order-service` is the checkout orchestrator. It receives order requests, reserves stock through `product-service`, processes payment through `payment-service`, persists the order in H2, and publishes a notification event to RabbitMQ.

## Stack

- Spring Boot `4.0.4`
- Java `25`
- Maven Wrapper
- Spring Web MVC
- Spring RestClient
- Spring Data JPA with H2
- Spring AMQP with RabbitMQ
- Actuator, Micrometer, and OpenTelemetry

## Observability Strategy

This service represents the Spring Boot 4 native OpenTelemetry path.

Main components:

- `spring-boot-starter-opentelemetry`
- OTLP export for traces, metrics, and logs through `management.*` properties
- `opentelemetry-logback-appender-1.0`, installed programmatically
- `datasource-micrometer-spring-boot` for DataSource instrumentation and SQL spans
- OpenTelemetry conventions for JVM and HTTP server metrics
- `TraceIdFilter` returning `X-Trace-Id`
- `ContextPropagatingTaskDecorator` for async context propagation
- RabbitMQ trace propagation through observation-enabled `RabbitTemplate`
- Custom checkout counters tagged by `result=success|error`

## Runtime Configuration

Default port:

```text
8080
```

Important properties:

```text
spring.application.name=order-service
spring.threads.virtual.enabled=true
microservices.product.url=http://localhost:8081
microservices.payment.url=http://localhost:8082
management.tracing.sampling.probability=1.0
```

In Docker Compose, service URLs point to internal Docker DNS names and OTLP endpoints point to `grafana-lgtm:4318`.

## Endpoint

Create an order:

```http
POST /orders
Content-Type: application/json
```

Example:

```json
{
  "productId": "product-001",
  "quantity": 2,
  "unitPrice": 49.90,
  "customerId": "customer-001",
  "currency": "BRL",
  "paymentMethod": "credit_card"
}
```

Relevant responses:

- `201 Created`: order created.
- `409 Conflict`: reserved quantity differs from requested quantity.
- `402 Payment Required`: payment failed.

## Functional Flow

1. Receive `POST /orders`.
2. Reserve stock in `product-service`.
3. Process payment in `payment-service`.
4. Confirm or cancel the reservation.
5. Persist the order in H2.
6. Publish a notification message to `notification.events`.

## Integrations

- HTTP client calls to `product-service`.
- HTTP client calls to `payment-service`.
- RabbitMQ producer for `notification-service`.
- H2 in-memory database.
- OTLP export to Grafana LGTM.

## Run Locally

### Start required infrastructure:

```bash
docker compose up -d grafana-lgtm rabbit
```

### 1. Run the service:

```bash
./mvnw spring-boot:run
```

### 2. Run through Compose:

```bash
docker compose up --build order-service
```

### When finished, stop the Compose stack:

```bash
docker compose down
```

## POC Analysis

- Strong coverage across traces, metrics, logs, SQL spans, async context, and RabbitMQ propagation.
- Less operationally opaque than a Java Agent because observability behavior is explicit in application configuration.
- More application coupling than zero-code instrumentation.
- Useful baseline for comparing Spring Boot 4 native OpenTelemetry against Spring Boot 3 approaches.

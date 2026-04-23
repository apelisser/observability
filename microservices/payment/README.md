# Payment Service

`payment-service` simulates payment processing. It receives an HTTP request, determines success or failure using a configurable rate, and returns the payment result.

## Stack

- Spring Boot `3.5.13`
- Java `21`
- Maven Wrapper
- Spring Web
- OpenTelemetry Spring Boot Starter

## Observability Strategy

This service represents the OpenTelemetry Spring Boot Starter approach:

- `opentelemetry-spring-boot-starter`
- `opentelemetry-instrumentation-bom` version `2.26.1`
- `otel.*` properties for service name and OTLP endpoint
- `TraceIdFilter` using the OpenTelemetry API to return `X-Trace-Id`
- log correlation pattern with `trace_id` and `span_id`

This approach sits between the Java Agent and Spring Boot native instrumentation: it requires application dependencies, but removes a large amount of manual instrumentation work.

## Runtime Configuration

Application port:

```text
8082
```

Important properties:

```text
spring.application.name=payment-service
payment.success.rate=99
otel.service.name=payment-service
otel.exporter.otlp.endpoint=http://localhost:4318
```

In Docker Compose:

- `PAYMENT_SUCCESS_RATE=70`
- `OTEL_EXPORTER_OTLP_ENDPOINT=http://grafana-lgtm:4318`
- `OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf`

## Endpoint

Process payment:

```http
POST /payments
Content-Type: application/json
```

Example:

```json
{
  "amount": 99.80,
  "currency": "BRL",
  "paymentMethod": "credit_card",
  "customerId": "customer-001"
}
```

Response:

```json
{
  "paymentId": "uuid",
  "status": "SUCCESS",
  "message": "Payment successful"
}
```

The `status` value can be `SUCCESS` or `FAILED`.

## Functional Flow

1. Receive `POST /payments`.
2. Generate a `paymentId`.
3. Determine success using `payment.success.rate`.
4. Return payment status and message.

## Integrations

- Called by `order-service` during checkout.
- Exports telemetry to Grafana LGTM through OTLP.

## Run Locally

### 1. Run with the default success rate:

```bash
./mvnw spring-boot:run
```

### 2. Run with a custom success rate:

```bash
PAYMENT_SUCCESS_RATE=70 ./mvnw spring-boot:run
```

### 3. Run through Compose:

```bash
docker compose up --build payment-service
```

### When finished, stop the Compose stack:

```bash
docker compose down
```

## POC Analysis

- Simpler than wiring Micrometer Tracing and exporters manually.
- More application-coupled than the Java Agent approach.
- Gives direct access to OpenTelemetry APIs when needed.
- Useful for comparing HTTP propagation and log correlation with limited setup.

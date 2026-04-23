# Notification Service

`notification-service` consumes notification messages from RabbitMQ. It listens to email and SMS queues, simulates notification delivery, and emits logs that can be correlated with distributed traces.

## Stack

- Spring Boot `3.5.13`
- Java `25`
- Maven Wrapper
- Spring AMQP
- Actuator
- Micrometer Tracing bridge to OpenTelemetry
- OpenTelemetry OTLP exporter
- Micrometer OTLP registry

## Observability Strategy

This service represents an explicit Spring/Micrometer approach without the Java Agent and without the OpenTelemetry Spring Boot Starter:

- `micrometer-tracing-bridge-otel` connects Micrometer Tracing to OpenTelemetry.
- `opentelemetry-exporter-otlp` exports traces.
- `micrometer-registry-otlp` exports metrics.
- `opentelemetry-logback-appender-1.0` exports logs.
- `management.*` properties configure traces, logs, and metrics through OTLP.
- `spring.rabbitmq.listener.simple.observation-enabled=true` continues traces received through RabbitMQ headers.

This approach is useful when the team wants explicit Spring-native configuration and does not want runtime agent dependency.

## Runtime Configuration

Default port:

```text
8083
```

Important properties:

```text
spring.application.name=notification-service
spring.threads.virtual.enabled=true
spring.rabbitmq.virtual-host=observability
spring.rabbitmq.listener.simple.prefetch=5
spring.rabbitmq.listener.simple.concurrency=3
spring.rabbitmq.listener.simple.max-concurrency=10
management.tracing.sampling.probability=1.0
```

In Docker Compose, RabbitMQ host is overridden to `rabbit` and OTLP endpoints point to `grafana-lgtm:4318`.

## Messaging

Exchange:

```text
notification.events
```

Queues:

```text
notification.email.send.queue
notification.sms.send.queue
```

Routing keys:

```text
notification.email.send
notification.sms.send
```

Consumers:

- `emailConsumer`: consumes email notifications.
- `smsConsumer`: consumes SMS notifications.

## Functional Flow

1. `order-service` publishes a message to `notification.events`.
2. RabbitMQ routes the message to the email or SMS queue.
3. The consumer receives the message.
4. The service simulates delivery with a 2-second delay.
5. Logs are emitted during receive and completion.

## Integrations

- RabbitMQ consumer.
- Receives messages from `order-service`.
- Exports telemetry to Grafana LGTM through OTLP.

## Run Locally

### Start dependencies:

```bash
docker compose up -d rabbit grafana-lgtm
```

### 1. Run the service:

```bash
./mvnw spring-boot:run
```

### 2. Run through Compose:

```bash
docker compose up --build notification-service
```

### When finished, stop the Compose stack:

```bash
docker compose down
```

## POC Analysis

- Strong baseline for evaluating context propagation over messaging.
- More explicit control than Java Agent instrumentation.
- More application wiring than starter-based approaches.
- Useful for comparing RabbitMQ producer propagation from Spring Boot 4 with consumer propagation in Spring Boot 3.5.

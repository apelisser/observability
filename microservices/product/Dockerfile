FROM eclipse-temurin:17-jdk-noble AS builder

WORKDIR /build

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -q

COPY src/ src/
RUN ./mvnw clean package -q -DskipTests

FROM eclipse-temurin:17-jre-noble AS runtime

WORKDIR /app

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

COPY --from=builder /build/target/*.jar app.jar

# Add OpenTelemetry agent
RUN mkdir -p /opt/otel && chown -R appuser:appgroup /opt/otel
ARG OTEL_VERSION=2.26.1
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_VERSION}/opentelemetry-javaagent.jar /opt/otel/opentelemetry-javaagent.jar
RUN chown appuser:appgroup /opt/otel/opentelemetry-javaagent.jar

USER appuser

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]

#!/bin/bash

OTEL_DIR="$(dirname "$0")/../otel/agent"
OTEL_JAR="$OTEL_DIR/opentelemetry-javaagent.jar"
OTEL_VERSION=2.26.1

mkdir -p "$OTEL_DIR"

if [ ! -f "$OTEL_JAR" ]; then
  echo "Downloading OpenTelemetry agent..."
  curl -L -o "$OTEL_JAR" \
  https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_VERSION}/opentelemetry-javaagent.jar
fi
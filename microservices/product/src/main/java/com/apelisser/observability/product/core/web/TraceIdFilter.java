package com.apelisser.observability.product.core.web;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter responsible for adding the trace ID to the HTTP response headers.
 *
 * <p>This implementation is based on the OpenTelemetry API, which provides a standard way to trace
 * requests across microservices. To use this filter, you need to add the dependency
 * <code>io.opentelemetry:opentelemetry-api</code> to your project.
 *
 * <p>Note that this filter can only work if the OpenTelemetry API is properly configured and
 * instrumentation is enabled.
 */
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String traceId = getTraceId();
        if (traceId != null) {
            response.setHeader("X-Trace-Id", traceId);
        }

        filterChain.doFilter(request, response);
    }

    private String getTraceId() {
        SpanContext spanContext = Span.current().getSpanContext();
        return (spanContext != null && spanContext.isValid())
            ? spanContext.getTraceId()
            : null;
    }

}
package com.apelisser.observability.payment.core.web;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
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

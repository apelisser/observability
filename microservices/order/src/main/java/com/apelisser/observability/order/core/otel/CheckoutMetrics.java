package com.apelisser.observability.order.core.otel;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CheckoutMetrics {

    private final Counter successCounter;
    private final Counter errorCounter;

    public CheckoutMetrics(MeterRegistry registry) {
        this.successCounter = Counter.builder("order")
            .description("Order creation success counter")
            .tag("result", "success")
            .register(registry);

        this.errorCounter = Counter.builder("order")
            .description("Order creation error counter")
            .tag("result", "error")
            .register(registry);
    }

    public void incrementSuccessCounter() {
        successCounter.increment();
    }

    public void incrementErrorCounter() {
        errorCounter.increment();
    }

}

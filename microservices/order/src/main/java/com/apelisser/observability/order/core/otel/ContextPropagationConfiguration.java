package com.apelisser.observability.order.core.otel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.ContextPropagatingTaskDecorator;

/**
 * This configuration class was extracted from:
 * https://spring.io/blog/2025/11/18/opentelemetry-with-spring-boot
 */
@Configuration(proxyBeanMethods = false)
public class ContextPropagationConfiguration {

    @Bean
    ContextPropagatingTaskDecorator contextPropagatingTaskDecorator() {
        return new ContextPropagatingTaskDecorator();
    }

}

package com.apelisser.observability.order.core.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.amqp.autoconfigure.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "notification.events";

    public static final List<String> ROUTING_KEYS = List.of(
        "notification.email.send",
        "notification.sms.send"
    );

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    /**
     * Customizes RabbitTemplate with trace propagation support (observation-enabled).
     * WARNING: this bean ignores settings defined in application.properties/yaml
     * (spring.rabbitmq.template.*). Any required property must be configured
     * explicitly here. Prefer RabbitTemplateConfigurer to inherit Spring Boot settings.
     */
    @Bean
    public RabbitTemplate rabbitTemplateManual(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setObservationEnabled(true);
        template.setMessageConverter(messageConverter);
        return template;
    }

    /**
     * Customizes RabbitTemplate while preserving all settings defined in application.properties/yaml
     * (for example, spring.rabbitmq.template.*), including observation-enabled for trace propagation.
     * Using RabbitTemplateConfigurer guarantees that Spring Boot properties are applied
     * before this bean's additional customizations.
     */
    @Bean
    @Primary
    public RabbitTemplate rabbitTemplateConfigured(RabbitTemplateConfigurer configurer, ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template, connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

}

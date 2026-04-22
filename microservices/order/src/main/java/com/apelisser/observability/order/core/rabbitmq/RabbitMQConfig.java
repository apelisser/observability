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
     * Customiza o RabbitTemplate com suporte a propagação de trace (observation-enabled).
     * ATENÇÃO: este bean ignora as configurações definidas em application.properties/yaml
     * (spring.rabbitmq.template.*). Qualquer propriedade necessária deve ser configurada
     * explicitamente aqui. Prefira usar RabbitTemplateConfigurer para herdar as configs do Spring Boot.
     */
    @Bean
    public RabbitTemplate rabbitTemplateManual(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setObservationEnabled(true);
        template.setMessageConverter(messageConverter);
        return template;
    }

    /**
     * Customiza o RabbitTemplate mantendo todas as configurações definidas em application.properties/yaml
     * (ex: spring.rabbitmq.template.*), incluindo observation-enabled para propagação de trace.
     * O uso do RabbitTemplateConfigurer garante que as propriedades do Spring Boot sejam aplicadas
     * antes das customizações adicionais deste bean.
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

package com.apelisser.observability.notification.core.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "notification.events";

    public static final String EMAIL_QUEUE = "notification.email.send.queue";
    public static final String EMAIL_ROUTING_KEY = "notification.email.send";

    public static final String SMS_QUEUE = "notification.sms.send.queue";
    public static final String SMS_ROUTING_KEY = "notification.sms.send";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange notificationExchange) {
        return BindingBuilder
            .bind(emailQueue)
            .to(notificationExchange)
            .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Queue smsQueue() {
        return QueueBuilder.durable(SMS_QUEUE).build();
    }

    @Bean
    public Binding smsBinding(Queue smsQueue, TopicExchange notificationExchange) {
        return BindingBuilder
            .bind(smsQueue)
            .to(notificationExchange)
            .with(SMS_ROUTING_KEY);
    }

}

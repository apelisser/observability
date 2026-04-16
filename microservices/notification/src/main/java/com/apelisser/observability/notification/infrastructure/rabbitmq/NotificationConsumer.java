package com.apelisser.observability.notification.infrastructure.rabbitmq;

import com.apelisser.observability.notification.core.rabbitmq.RabbitMQConfig;
import com.apelisser.observability.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void emailConsumer(String message) {
        notificationService.sendNotification(message, "email");
    }

    @RabbitListener(queues = RabbitMQConfig.SMS_QUEUE)
    public void smsConsumer(String message) {
        notificationService.sendNotification(message, "sms");
    }

}

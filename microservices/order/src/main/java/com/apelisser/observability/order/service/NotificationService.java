package com.apelisser.observability.order.service;

import com.apelisser.observability.order.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

import static com.apelisser.observability.order.core.rabbitmq.RabbitMQConfig.*;

@Slf4j
@Service
public class NotificationService {

    private final Semaphore limit = new Semaphore(4);

    private final RabbitTemplate rabbitTemplate;

    public NotificationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notify(Notification notification) {
        log.info("Sending notification: {}", notification);
        String routingKey = getRandomRoutingKey();
        sendNotification(routingKey, notification);
    }

    @Async
    public void notifyAsync(Notification notification) {
        boolean acquired = false;
        try {
            limit.acquire();
            acquired = true;

            log.info("Sending async notification: {}", notification);
            String routingKey = getRandomRoutingKey();
            sendNotification(routingKey, notification);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while waiting for notification", e);
        } finally {
            if (acquired) {
                limit.release();
            }
        }
    }

    private void sendNotification(String routingKey, Notification notification) {
        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, notification);
    }

    private String getRandomRoutingKey() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, ROUTING_KEYS.size());
        return ROUTING_KEYS.get(randomIndex);
    }

}

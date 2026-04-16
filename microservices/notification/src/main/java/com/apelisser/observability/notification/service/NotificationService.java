package com.apelisser.observability.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendNotification(String message, String channel) {
        log.info("Sending notification | channel={}, message={}", channel, message);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Notification interrupted");
            return;
        }

        log.info("Notification sent");
    }

}

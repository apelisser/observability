package com.apelisser.observability.order.service;

import com.apelisser.observability.order.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

@Slf4j
@Service
public class NotificationService {

    private final Semaphore limit = new Semaphore(2);

    public void notify(Notification notification) {
        // TODO
        log.info("Sending notification: {}", notification);
    }

    @Async
    public void notifyAsync(Notification notification) {
        boolean acquired = false;
        try {
            limit.acquire();
            acquired = true;

            // TODO
            log.info("Sending async notification: {}", notification);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while waiting for notification", e);
        } finally {
            if (acquired) {
                limit.release();
            }
        }
    }

}

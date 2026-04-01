package com.apelisser.observability.servicea.infrastructure.document;

import com.apelisser.observability.servicea.domain.document.DocumentStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DocumentStorageFake implements DocumentStorage {

    private static final AtomicInteger UPLOAD_ERROR_PERCENTAGE = new AtomicInteger(0);

    @Override
    public void save(String filename, InputStream content) {
        long size = this.calculateSize(content);
        this.simulateUploadToStorage(size);
    }

    private long calculateSize(InputStream content) {
        long size = 0;
        byte[] buffer = new byte[8192];
        int read;

        try {
            while ((read = content.read(buffer)) != -1) {
                size += read;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to calculate size of document", e);
        }
        return size;
    }

    private void simulateUploadToStorage(long size) {
        double minSpeed = 512 * 1024;
        double maxSpeed = 2 * 1024 * 1024;
        double speed = ThreadLocalRandom.current().nextDouble(minSpeed, maxSpeed);

        String speedFormatted = String.format("%.2f", speed / (1024 * 1024));
        log.info("Upload speed: {} MB/s", speedFormatted);

        long timeToUpload = (long) (size / speed * 1000);

        boolean isUploadError = isUploadError();

        try {

            if (isUploadError) {
                double delayPercentageReduction = ThreadLocalRandom.current().nextDouble(0.1, 1.0);
                int timeToErrorUpload = (int) (timeToUpload * delayPercentageReduction);

                if (timeToErrorUpload > 0) {
                    Thread.sleep(timeToErrorUpload);
                }

                throw new RuntimeException("Failed to upload document");
            }

            Thread.sleep(timeToUpload);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isUploadError() {
        return ThreadLocalRandom.current().nextInt(100) < UPLOAD_ERROR_PERCENTAGE.get();
    }

    public static void updateUploadErrorPercentage(int errorPercentage) {
        if (errorPercentage < 0) {
            errorPercentage = 0;
        }

        if (errorPercentage > 100) {
            errorPercentage = 100;
        }

        UPLOAD_ERROR_PERCENTAGE.set(errorPercentage);
    }

}

package com.apelisser.observability.serviceb.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/tests")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/ping")
    public String test() {
        log.info("Ping received");
        return "pong";
    }

    @GetMapping("/sleeping")
    public String sleep(@RequestParam Integer seconds) {
        if (seconds != null && seconds > 0) {
            try {
                log.info("Sleeping for {} seconds", seconds);
                Thread.sleep(Duration.ofSeconds(seconds).toMillis());
            } catch (InterruptedException e) {
                log.error("Sleeping interrupted", e);
            }
        }
        log.info("Sleeping completed");
        return "sleeping";
    }

    @GetMapping("/error")
    public ResponseEntity<?> error() {
        log.info("Error received");


        return ResponseEntity
            .status(404)
            .build();
    }


}

package com.apelisser.observability.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

@SpringBootApplication
public class NotificationApplication {

	public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
		SpringApplication.run(NotificationApplication.class, args);
	}

}

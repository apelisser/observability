package com.apelisser.observability.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

@SpringBootApplication
@EnableAsync
public class OrderApplication {

	public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
		SpringApplication.run(OrderApplication.class, args);
	}

}

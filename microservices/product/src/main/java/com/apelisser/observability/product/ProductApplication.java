package com.apelisser.observability.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
		SpringApplication.run(ProductApplication.class, args);
	}

}

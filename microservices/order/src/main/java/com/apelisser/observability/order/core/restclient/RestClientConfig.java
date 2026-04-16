package com.apelisser.observability.order.core.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient productRestClient(
        RestClient.Builder builder,
        @Value("${microservices.product.url}") String productServiceUrl
    ) {
        var httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();
        new JdkClientHttpRequestFactory(httpClient);

        return builder
            .baseUrl(productServiceUrl)
            .requestFactory(new JdkClientHttpRequestFactory(httpClient))
            .build();
    }

    @Bean
    public RestClient paymentRestClient(
        RestClient.Builder builder,
        @Value("${microservices.payment.url}") String paymentServiceUrl
    ) {
        var httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();
        new JdkClientHttpRequestFactory(httpClient);

        return builder
            .baseUrl(paymentServiceUrl)
            .requestFactory(new JdkClientHttpRequestFactory(httpClient))
            .build();
    }

}

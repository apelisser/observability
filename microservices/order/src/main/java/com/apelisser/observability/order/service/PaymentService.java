package com.apelisser.observability.order.service;

import com.apelisser.observability.order.model.Payment;
import com.apelisser.observability.order.model.PaymentResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class PaymentService {

    private final RestClient paymentClient;

    public PaymentService(RestClient paymentRestClient) {
        this.paymentClient = paymentRestClient;
    }

    public PaymentResult processPayment(Payment paymentToProcess) {
        log.info("Processing payment: {}", paymentToProcess);

        return paymentClient.post()
            .uri("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .body(paymentToProcess)
            .retrieve()
            .body(PaymentResult.class);
    }

}

package com.apelisser.observability.order.service;

import com.apelisser.observability.order.model.Payment;
import com.apelisser.observability.order.model.PaymentResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PaymentService {

    public PaymentResult processPayment(Payment paymentToProcess) {
        // TODO
        log.info("Processing payment: {}", paymentToProcess);
        return PaymentResult.builder()
            .paymentId(UUID.randomUUID().toString())
            .status("SUCCESS")
            .message("Payment processed successfully")
            .build();
    }

}

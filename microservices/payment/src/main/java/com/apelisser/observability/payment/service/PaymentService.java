package com.apelisser.observability.payment.service;

import com.apelisser.observability.payment.model.PaymentInput;
import com.apelisser.observability.payment.model.PaymentOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.apelisser.observability.payment.model.PaymentProcessingStatus.FAILED;
import static com.apelisser.observability.payment.model.PaymentProcessingStatus.SUCCESS;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Value("${payment.success.rate}")
    private int paymentSuccessRate;

    public PaymentOutput processPayment(PaymentInput payment) {
        log.info("Payment processing started for customerID={} with method={}",
            payment.getCustomerId(),
            payment.getPaymentMethod());

        return isPaymentSuccessful()
            ? successResult()
            : failureResult();
    }

    private PaymentOutput successResult() {
        UUID paymentId = UUID.randomUUID();

        log.info("Payment processing for ID={} was successful", paymentId);

        PaymentOutput output = new PaymentOutput();
        output.setPaymentId(paymentId);
        output.setStatus(SUCCESS);
        output.setMessage("Payment successful");
        return output;
    }

    private PaymentOutput failureResult() {
        UUID paymentId = UUID.randomUUID();

        log.warn("Payment processing for ID={} failed.", paymentId);

        PaymentOutput output = new PaymentOutput();
        output.setPaymentId(paymentId);
        output.setStatus(FAILED);
        output.setMessage("Payment failed");
        return output;
    }

    public boolean isPaymentSuccessful() {
        if (paymentSuccessRate <= 0) return false;
        if (paymentSuccessRate >= 100) return true;

        return ThreadLocalRandom.current().nextInt(0, 100) <  paymentSuccessRate;
    }

}

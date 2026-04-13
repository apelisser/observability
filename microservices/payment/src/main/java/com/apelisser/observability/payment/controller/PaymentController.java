package com.apelisser.observability.payment.controller;

import com.apelisser.observability.payment.model.PaymentInput;
import com.apelisser.observability.payment.model.PaymentOutput;
import com.apelisser.observability.payment.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentOutput processPayment(@RequestBody PaymentInput payment) {
        return paymentService.processPayment(payment);
    }

}

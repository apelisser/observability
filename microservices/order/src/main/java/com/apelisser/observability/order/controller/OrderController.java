package com.apelisser.observability.order.controller;

import com.apelisser.observability.order.application.CheckoutService;
import com.apelisser.observability.order.exception.PaymentFailedException;
import com.apelisser.observability.order.exception.ProductOutOfStockException;
import com.apelisser.observability.order.model.OrderInput;
import com.apelisser.observability.order.model.OrderOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CheckoutService checkoutService;

    public OrderController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderInput input) {
        try {
            OrderOutput orderOutput = checkoutService.checkout(input);

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderOutput);
        } catch (ProductOutOfStockException e) {
            return ResponseEntity
                .status(409)
                .build();
        } catch (PaymentFailedException e) {
            return ResponseEntity
                .status(402)
                .build();
        }
    }

}

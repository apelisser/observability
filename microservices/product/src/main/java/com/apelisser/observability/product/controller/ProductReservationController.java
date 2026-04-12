package com.apelisser.observability.product.controller;

import com.apelisser.observability.product.model.ProductReservationInput;
import com.apelisser.observability.product.model.ReservationOutput;
import com.apelisser.observability.product.service.ProductReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products/{productId}/reservations")
public class ProductReservationController {

    private final ProductReservationService productReservationService;

    public ProductReservationController(ProductReservationService productReservationService) {
        this.productReservationService = productReservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationOutput reserve(@PathVariable String productId, @Valid @RequestBody ProductReservationInput reservation) {
        return productReservationService.reserve(productId, reservation.getQuantity());
    }

    @PatchMapping("/{reservationId}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@PathVariable String productId, @PathVariable UUID reservationId) {
        productReservationService.confirmReservation(productId, reservationId);
    }

    @PatchMapping("/{reservationId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable String productId, @PathVariable UUID reservationId) {
        productReservationService.cancelReservation(productId, reservationId);
    }

}

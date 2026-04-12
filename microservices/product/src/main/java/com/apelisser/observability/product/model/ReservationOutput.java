package com.apelisser.observability.product.model;

import java.util.Objects;
import java.util.UUID;

public record ReservationOutput (UUID reservationId, String productId, int reservedQuantity) {

    public ReservationOutput {
        Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        Objects.requireNonNull(productId, "Product ID cannot be null");
        if (reservedQuantity < 0) {
            throw new IllegalArgumentException("Reserved quantity cannot be negative");
        }
    }

}

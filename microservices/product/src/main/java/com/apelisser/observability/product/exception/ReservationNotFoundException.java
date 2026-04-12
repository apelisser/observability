package com.apelisser.observability.product.exception;

import java.io.Serial;
import java.util.UUID;

public class ReservationNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7513059234074488163L;

    public ReservationNotFoundException(UUID reservationId) {
        super("Reservation not found for ID: " + reservationId.toString());
    }

}

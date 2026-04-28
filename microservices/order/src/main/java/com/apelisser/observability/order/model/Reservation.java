package com.apelisser.observability.order.model;

public record Reservation(int quantity) {

    public Reservation {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    public static Reservation of(int quantity) {
        return new Reservation(quantity);
    }

}

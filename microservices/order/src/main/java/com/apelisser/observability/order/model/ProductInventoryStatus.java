package com.apelisser.observability.order.model;

public enum ProductInventoryStatus {

    AVAILABLE,
    UNAVAILABLE;

    public boolean isAvailable() {
        return this == AVAILABLE;
    }

    public boolean isUnavailable() {
        return this == UNAVAILABLE;
    }

}

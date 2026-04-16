package com.apelisser.observability.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservationResult {

    private String reservationId;
    private String productId;
    private Integer requestedQuantity;
    private Integer reservedQuantity;

}

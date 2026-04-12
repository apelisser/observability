package com.apelisser.observability.product.model;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductReservationInput {

    @Positive
    private Integer quantity;

}

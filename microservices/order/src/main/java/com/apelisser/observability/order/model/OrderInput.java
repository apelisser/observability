package com.apelisser.observability.order.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderInput {

    private String productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String customerId;
    private String currency;
    private String paymentMethod;

}

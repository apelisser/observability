package com.apelisser.observability.order.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class Payment {

    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String customerId;

}

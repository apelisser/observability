package com.apelisser.observability.order.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class OrderOutput {

    private String orderId;
    private String customerId;
    private String productId;
    private Integer quantity;

}

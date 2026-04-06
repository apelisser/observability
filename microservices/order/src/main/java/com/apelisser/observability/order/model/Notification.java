package com.apelisser.observability.order.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Notification {

    private String customerId;
    private String type;
    private String message;
    private String orderId;

}

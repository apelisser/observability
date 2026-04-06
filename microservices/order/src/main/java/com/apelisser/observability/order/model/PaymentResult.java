package com.apelisser.observability.order.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResult {

    private String paymentId;
    private String status;
    private String message;

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }
}

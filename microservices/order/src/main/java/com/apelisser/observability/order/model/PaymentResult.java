package com.apelisser.observability.order.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResult {

    private String paymentId;
    private String status;
    private String message;

    public PaymentResult() {
    }

    @Builder
    public PaymentResult(String paymentId, String status, String message) {
        this.paymentId = paymentId;
        this.status = status;
        this.message = message;
    }

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }
}

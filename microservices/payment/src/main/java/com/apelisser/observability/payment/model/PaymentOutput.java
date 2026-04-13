package com.apelisser.observability.payment.model;

import java.util.UUID;

public class PaymentOutput {

    private UUID paymentId;
    private PaymentProcessingStatus status;
    private String message;

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentProcessingStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentProcessingStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

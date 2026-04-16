package com.apelisser.observability.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private UUID id;
    private String productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String customerId;
    private String paymentId;

    @Builder
    public static Order brandNew(UUID id, String productId, Integer quantity, BigDecimal unitPrice,
            String customerId, String paymentId) {
        Order order = new Order();
        order.setId(id);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setUnitPrice(unitPrice);
        order.setCustomerId(customerId);
        order.setPaymentId(paymentId);
        return order;
    }

}

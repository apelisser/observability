package com.apelisser.observability.product.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "product_reservation")
public class ProductReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String productId;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

}

package com.apelisser.observability.product.repository;

import com.apelisser.observability.product.entity.ProductReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductReservationRepository extends JpaRepository<ProductReservation, UUID> {

    Optional<ProductReservation> findByIdAndProductId(
        @Param("id") UUID reservationId,
        @Param("productId") String productId);

}

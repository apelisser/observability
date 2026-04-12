package com.apelisser.observability.product.service;

import com.apelisser.observability.product.entity.ProductReservation;
import com.apelisser.observability.product.entity.ReservationStatus;
import com.apelisser.observability.product.exception.ReservationNotFoundException;
import com.apelisser.observability.product.model.ReservationOutput;
import com.apelisser.observability.product.repository.ProductReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class ProductReservationService {

    private final ProductReservationRepository reservationRepository;

    public ProductReservationService(ProductReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationOutput reserve(String productId, int quantity) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        log.info("Reserving quantity: {} for productId: {}", productId, quantity);
        ProductReservation reservation = createReservation(productId, quantity);
        log.info("Created reservation: {}", reservation.getId());

        return new ReservationOutput(
            reservation.getId(),
            reservation.getProductId(),
            reservation.getQuantity()
        );
    }

    @Transactional
    public void confirmReservation(String productId, UUID reservationId) {
        Objects.requireNonNull(reservationId, "Reservation ID cannot be null");

        log.info("Confirming reservation: {}", reservationId);
        updateStatus(productId, reservationId, ReservationStatus.CONFIRMED);
        log.info("Confirmed reservation");
    }

    @Transactional
    public void cancelReservation(String productId, UUID reservationId) {
        Objects.requireNonNull(reservationId, "Reservation ID cannot be null");

        log.info("Canceling reservation: {}", reservationId);
        updateStatus(productId, reservationId, ReservationStatus.CANCELLED);
        log.info("Canceled reservation");
    }

    private ProductReservation createReservation(String productId, int quantity) {
        ProductReservation reservation = new ProductReservation();
        reservation.setProductId(productId);
        reservation.setQuantity(quantity);
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationRepository.saveAndFlush(reservation);
    }

    private void updateStatus(String productId, UUID reservationId, ReservationStatus status) {
        ProductReservation reservation = reservationRepository.findByIdAndProductId(reservationId, productId)
            .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        reservation.setStatus(status);
        reservationRepository.saveAndFlush(reservation);
    }

}

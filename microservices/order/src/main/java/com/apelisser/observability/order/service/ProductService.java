package com.apelisser.observability.order.service;

import com.apelisser.observability.order.model.ReservationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class ProductService {

    private final RestClient productClient;

    public ProductService(RestClient productRestClient) {
        this.productClient = productRestClient;
    }

    public ReservationResult reserveStock(String productId, Integer quantity) {
        log.info("Reserving stock for product: {}", productId);

        ReservationResult result = productClient.post()
            .uri("/products/{productId}/reservations", productId)
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"quantity\": \"" + quantity + "\"}")
            .retrieve()
            .body(ReservationResult.class);

        log.info("Reservation id: {}", result.getReservationId());
        return result;
    }

    public void confirmReservation(String productId, String reservationId) {
        log.info("Confirming reservation={} for product={}", reservationId, productId);

        productClient.patch()
            .uri("/products/{productId}/reservations/{reservationId}/confirm", productId, reservationId)
            .retrieve()
            .toBodilessEntity();
    }

    public void cancelReservation(String productId, String reservationId) {
        log.info("Cancelling reservation={} for product={}", reservationId, productId);

        productClient.patch()
            .uri("/products/{productId}/reservations/{reservationId}/cancel", productId, reservationId)
            .retrieve()
            .toBodilessEntity();
    }

}

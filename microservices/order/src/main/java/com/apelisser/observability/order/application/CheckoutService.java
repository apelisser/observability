package com.apelisser.observability.order.application;

import com.apelisser.observability.order.core.otel.CheckoutMetrics;
import com.apelisser.observability.order.entity.Order;
import com.apelisser.observability.order.exception.PaymentFailedException;
import com.apelisser.observability.order.exception.ProductOutOfStockException;
import com.apelisser.observability.order.model.Notification;
import com.apelisser.observability.order.model.OrderInput;
import com.apelisser.observability.order.model.OrderOutput;
import com.apelisser.observability.order.model.Payment;
import com.apelisser.observability.order.model.PaymentResult;
import com.apelisser.observability.order.model.ReservationResult;
import com.apelisser.observability.order.service.NotificationService;
import com.apelisser.observability.order.service.OrderService;
import com.apelisser.observability.order.service.PaymentService;
import com.apelisser.observability.order.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.Semaphore;

@Slf4j
@Service
public class CheckoutService {

    private final Semaphore notificationSemaphore = new Semaphore(100);

    private final OrderService orderService;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    private final CheckoutMetrics checkoutMetrics;

    public CheckoutService(OrderService orderService, ProductService productService, PaymentService paymentService,
            NotificationService notificationService, CheckoutMetrics checkoutMetrics) {
        this.orderService = orderService;
        this.productService = productService;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
        this.checkoutMetrics = checkoutMetrics;
    }

    @Transactional
    public OrderOutput checkout(OrderInput orderInput) {
        UUID orderId = UUID.randomUUID();
        try {
            ReservationResult reservation = productService.reserveStock(orderInput.getProductId(), orderInput.getQuantity());
            if (!orderInput.getQuantity().equals(reservation.getReservedQuantity())) {
                log.warn("Product {} is out of stock", orderInput.getProductId());
                throw new ProductOutOfStockException();
            }

            PaymentResult payment = processPayment(orderInput);

            if (payment.isSuccess()) {
                productService.confirmReservation(reservation.getProductId(), reservation.getReservationId());
            } else {
                productService.cancelReservation(reservation.getProductId(), reservation.getReservationId());
                log.warn("Payment failed for order {}", orderId);
                throw new PaymentFailedException();
            }

            Order newOrder = orderService.save(
                Order.builder()
                    .id(orderId)
                    .paymentId(payment.getPaymentId())
                    .quantity(orderInput.getQuantity())
                    .productId(orderInput.getProductId())
                    .unitPrice(orderInput.getUnitPrice())
                    .customerId(orderInput.getCustomerId())
                    .build()
            );

            notificationService.notifyAsync(
                Notification.builder()
                    .orderId(orderId.toString())
                    .customerId(orderInput.getCustomerId())
                    .type("SUCCESS")
                    .message("Payment successful")
                    .build()
            );

            OrderOutput orderOutput = OrderOutput.builder()
                .orderId(newOrder.getId().toString())
                .customerId(newOrder.getCustomerId())
                .productId(orderInput.getProductId())
                .quantity(orderInput.getQuantity())
                .build();

            checkoutMetrics.incrementSuccessCounter();
            return orderOutput;
        } catch (Exception e) {
            checkoutMetrics.incrementErrorCounter();
            throw e;
        }
    }

    private PaymentResult processPayment(OrderInput orderInput) {
        BigDecimal totalAmount = orderInput.getUnitPrice().multiply(new BigDecimal(orderInput.getQuantity()));
        Payment paymentInput = Payment.builder()
            .amount(totalAmount)
            .customerId(orderInput.getCustomerId())
            .currency(orderInput.getCurrency())
            .paymentMethod(orderInput.getPaymentMethod())
            .build();

        return paymentService.processPayment(paymentInput);
    }

}

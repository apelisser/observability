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
import com.apelisser.observability.order.model.ProductInventoryStatus;
import com.apelisser.observability.order.service.NotificationService;
import com.apelisser.observability.order.service.OrderService;
import com.apelisser.observability.order.service.PaymentService;
import com.apelisser.observability.order.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        try {
            this.checkStockAndReserveProduct(orderInput.getProductId(), orderInput.getQuantity());
            PaymentResult paymentResult = this.processPayment(orderInput);

            Order newOrder = orderService.save(
                Order.builder()
                    .paymentId(paymentResult.getPaymentId())
                    .quantity(orderInput.getQuantity())
                    .productId(orderInput.getProductId())
                    .unitPrice(orderInput.getUnitPrice())
                    .customerId(orderInput.getCustomerId())
                    .build()
            );

            notificationService.notifyAsync(
                Notification.builder()
                    .orderId(newOrder.getId().toString())
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

    private void checkStockAndReserveProduct(String productId, Integer quantity) {
        ProductInventoryStatus productStatus = productService.checkStock(productId);
        if (productStatus.isUnavailable()) {
            log.warn("Product {} is out of stock", productId);
            throw new ProductOutOfStockException();
        }

        productService.reserveStock(productId, quantity);
    }

    private PaymentResult processPayment(OrderInput orderInput) {
        BigDecimal totalAmount = orderInput.getUnitPrice().multiply(new BigDecimal(orderInput.getQuantity()));
        Payment paymentInput = Payment.builder()
            .amount(totalAmount)
            .customerId(orderInput.getCustomerId())
            .currency(orderInput.getCurrency())
            .paymentMethod(orderInput.getPaymentMethod())
            .build();

        PaymentResult paymentResult = paymentService.processPayment(paymentInput);
        if (!paymentResult.isSuccess()) {
            log.warn("Payment failed for order {}", orderInput.getProductId());
            throw new PaymentFailedException();
        }

        return paymentResult;
    }

}

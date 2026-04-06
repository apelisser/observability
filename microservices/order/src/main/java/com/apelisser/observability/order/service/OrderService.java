package com.apelisser.observability.order.service;

import com.apelisser.observability.order.entity.Order;
import com.apelisser.observability.order.model.OrderInput;
import com.apelisser.observability.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order orderInput) {
        return orderRepository.saveAndFlush(orderInput);
    }

}

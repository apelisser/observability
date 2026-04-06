package com.apelisser.observability.order.service;

import com.apelisser.observability.order.model.ProductInventoryStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService {

    public ProductInventoryStatus checkStock(String productId) {
        // TODO
        log.info("Checking stock for product: {}", productId);
        return ProductInventoryStatus.AVAILABLE;
    }

    public void reserveStock(String productId, Integer quantity) {
        // TODO
        log.info("Reserving stock for product: {}", productId);
    }

}

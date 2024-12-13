package com.damuzee.boot.spec.tenant;

import com.damuzee.boot.spec.tenant.lock.DistributedLock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @DistributedLock(name = "{#product.sku + ':'+ #product.storeId}", waitTime = 3000, tenantPrefix = true)
    public Order placeOrder(Long userId, Product product, long runTime) {
        log.info("placeOrder,user:{}, product:{}", userId, product.getSku());
        try {
            log.info("handling........");
            Thread.sleep(runTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Order("NO" + System.currentTimeMillis());
    }

    @RequiredArgsConstructor
    @Data
    public static class Product {

        private final String sku;
        private Long storeId = 1L;
    }

    @AllArgsConstructor
    @Data
    public static class Order {

        private String orderNo;
    }
}

package com.damuzee.boot.spec.tenant;

import static org.assertj.core.api.Assertions.assertThat;

import com.damuzee.boot.spec.tenant.lock.DistributedLockException;
import com.damuzee.boot.spec.util.DamuzeeArchContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;


@Slf4j
public class DistributeLockTestCase {

    private final OrderService orderService;
    private final RedissonClient redissonClient;

    public DistributeLockTestCase(OrderService orderService, RedissonClient redissonClient) {
        this.orderService = orderService;
        this.redissonClient = redissonClient;
    }

    public void test_smoke() {
        long runTime = 10;
        OrderService.Product product = new OrderService.Product("250500");
        DamuzeeArchContext.TENANT_ID.set("1");
        OrderService.Order placeOrder = orderService.placeOrder(1L, product, runTime);
        assertThat(placeOrder).isNotNull();
    }


    public void test_get_lock_failed() throws InterruptedException {
        AtomicInteger failedCount = new AtomicInteger(0);

        ExecutorService exec = Executors.newFixedThreadPool(2);
        exec.execute(new Runnable() {
            @Override
            public void run() {
                long runTime = 3500;
                OrderService.Product product = new OrderService.Product("250500");
                DamuzeeArchContext.TENANT_ID.set("1");
                OrderService.Order placeOrder = orderService.placeOrder(1L, product, runTime);
            }
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        exec.execute(new Runnable() {
            @Override
            public void run() {
                long runTime = 3500;
                OrderService.Product product = new OrderService.Product("250500");
                DamuzeeArchContext.TENANT_ID.set("1");
                try {
                    OrderService.Order placeOrder = orderService.placeOrder(1L, product, runTime);
                } catch (DistributedLockException e) {
                    failedCount.getAndIncrement();
                }
            }
        });

        exec.shutdown();
        exec.awaitTermination(50, TimeUnit.SECONDS);

        assertThat(failedCount.get()).isEqualTo(1);
    }

    /**
     * <pre>
     * 时间线： ----100------200-------300---------500-----------------------1000------------->T
     * 用户1:      |----------执行中----------------|
     *          获取锁                       执行完成，释放锁
     *
     * 用户2:              |-获取锁,等待用户1释放锁--|--------执行中------------|
     *                 开始执行                 获取锁成功              执行成功，释放锁
     * </pre>
     */
    public void test_concurrency_of_2() throws InterruptedException {
        String productSku = "250500";
        ExecutorService exec = Executors.newFixedThreadPool(16);
        Long user1 = 1L;
        Long user2 = 2L;
        List<OrderService.Order> orders = new ArrayList<>();

        // 用户 1 下单
        exec.execute(new Runnable() {
            @Override
            public void run() {
                long runTime = 500;
                log.info("用户: {}, 对产品: {} 下单，耗时 {} 毫秒", user1, productSku, runTime);
                OrderService.Product product = new OrderService.Product(productSku);
                DamuzeeArchContext.TENANT_ID.set("1");
                OrderService.Order placeOrder = orderService.placeOrder(user1, product, runTime);
                orders.add(placeOrder);
            }
        });

        try {
            log.info("等待用户 {} ,300 毫秒", user1);
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //2 用户 2 下单，由于获取锁的最大等待时长为 3 秒钟，会获取锁成功，下单成功。
        exec.execute(new Runnable() {
            @Override
            public void run() {
                long runTime = 500;
                log.info("用户: {}, 对产品: {} 下单，耗时 {} 毫秒", user2, productSku, runTime);
                OrderService.Product product = new OrderService.Product(productSku);
                DamuzeeArchContext.TENANT_ID.set("1");
                OrderService.Order placeOrder = orderService.placeOrder(user2, product, runTime);
                orders.add(placeOrder);
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exec.shutdown();
        exec.awaitTermination(50, TimeUnit.SECONDS);

        assertThat(orders.size()).isEqualTo(2);
    }

    /**
     * <p>模拟10个用户，每个用户处理耗时 500 毫秒，每个用户获取锁的最大等待时间为3000毫秒，因此有 10 - 3000/500 = 4 个用户未能获取锁成功，抛出异常。</p>
     */
    public void test_concurrency_of_10() throws InterruptedException {
        String productSku = "250500";
        ExecutorService exec = Executors.newFixedThreadPool(16);
        List<Future<OrderService.Order>> futures = new ArrayList<>(10);
        AtomicInteger failedCount = new AtomicInteger(0);

        for (int i = 1; i <= 10; i++) {
            final Long userId = (long) i;
            Future<OrderService.Order> future = exec.submit(() -> {
                DamuzeeArchContext.TENANT_ID.set("1");
                return orderService.placeOrder(userId, new OrderService.Product(productSku), 500);
            });
            futures.add(future);
        }

        for (Future<OrderService.Order> future : futures) {
            try {
                OrderService.Order placeOrder = future.get();
                log.info("placeOrder->{}", placeOrder);
            } catch (ExecutionException e) {
                e.printStackTrace();
                failedCount.getAndIncrement();
            }
        }

        exec.shutdown();
        exec.awaitTermination(50, TimeUnit.SECONDS);

        assertThat(failedCount.get()).isEqualTo(4);


    }
}

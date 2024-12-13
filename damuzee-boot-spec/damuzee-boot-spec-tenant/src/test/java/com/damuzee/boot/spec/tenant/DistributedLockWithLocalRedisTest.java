package com.damuzee.boot.spec.tenant;

import static org.assertj.core.api.Assertions.assertThat;

import com.damuzee.boot.spec.tenant.lock.EnableDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@EnableDistributedLock
@EnableAspectJAutoProxy
@ActiveProfiles({"local-redis"})
@SpringBootTest(classes = {OrderService.class})
public class DistributedLockWithLocalRedisTest {

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected RedissonClient redissonClient;

    @Test
    public void contextLoads() {
        assertThat(orderService).isNotNull();
        assertThat(redissonClient).isNotNull();
    }


    @Test
    public void test_smoke() {
        new DistributeLockTestCase(orderService, redissonClient).test_smoke();
    }

    @Test
    public void test_get_lock_failed() throws InterruptedException {
        new DistributeLockTestCase(orderService, redissonClient).test_get_lock_failed();
    }

    @Test
    public void test_concurrency_of_2() throws InterruptedException {
        new DistributeLockTestCase(orderService, redissonClient).test_concurrency_of_2();
    }


    @Test
    public void test_concurrency_of_10() throws InterruptedException {
        new DistributeLockTestCase(orderService, redissonClient).test_concurrency_of_10();
    }
}

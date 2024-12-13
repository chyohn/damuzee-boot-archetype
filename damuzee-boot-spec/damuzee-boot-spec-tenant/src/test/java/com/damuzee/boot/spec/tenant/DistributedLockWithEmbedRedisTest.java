package com.damuzee.boot.spec.tenant;

import static org.assertj.core.api.Assertions.assertThat;

import com.damuzee.boot.spec.tenant.lock.EnableDistributedLock;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

@Slf4j
@EnableDistributedLock
@EnableAspectJAutoProxy
@ActiveProfiles({"embed-redis"})
@SpringBootTest(classes = {OrderService.class})
public class DistributedLockWithEmbedRedisTest {

    static RedisServer redisServer = null;
    @Autowired
    OrderService orderService;
    @Resource
    RedissonClient redissonClient;

    @BeforeAll
    public static void setup() throws Exception {
        try {
            redisServer = new RedisServer(6380);
            redisServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public void test_concurrency_of_2() throws InterruptedException {
        new DistributeLockTestCase(orderService, redissonClient).test_concurrency_of_2();
    }


    @AfterAll
    public static void tearDown() throws Exception {
        redisServer.stop();
    }
}

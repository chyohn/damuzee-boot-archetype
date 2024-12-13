package com.damuzee.boot.spec.tenant.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

/**
 */
class ThreadPoolExecutorTest {

    @Test
    void test() {
        DamuzeeThreadPoolExecutor executor = new DamuzeeThreadPoolExecutor(10, 10,
            100, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("send-CallerRunsPolicy-thread-%s").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());
        executor.shutdown();
    }

}
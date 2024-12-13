package com.damuzee.boot.spec.tenant.lock;

import com.damuzee.boot.spec.tenant.redis.RedisKeyProperties;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration(RedissonAutoConfiguration.class)
@EnableConfigurationProperties({RedisKeyProperties.class})
public class DistributedLockAutoConfiguration {

    @Bean
    public DistributedLockAspectSupport distributedLockAspectSupport(RedissonClient redissonClient,
        RedisKeyProperties redisKeyProperties) {
        return new DistributedLockAspectSupport(redissonClient, redisKeyProperties);
    }
}

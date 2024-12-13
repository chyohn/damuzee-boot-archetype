package com.damuzee.boot.spec.tenant.redisson;

import io.lettuce.core.RedisClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import redis.clients.jedis.Jedis;

@Configuration
@EnableConfigurationProperties({RedisProperties.class})
public class RedisPoolAutoConfiguration {

    @Configuration
    @ConditionalOnClass(RedisClient.class)
    @ConditionalOnProperty(name = "spring.redis.client-type", havingValue = "lettuce", matchIfMissing = true)
    static class LettucePoolConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RedisProperties.Pool lettucePool(RedisProperties properties) {
            return properties.getLettuce().getPool();
        }

    }

    @Configuration
    @ConditionalOnClass({GenericObjectPool.class, JedisConnection.class, Jedis.class})
    @ConditionalOnProperty(name = "spring.redis.client-type", havingValue = "jedis", matchIfMissing = true)
    static class JedisPoolConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RedisProperties.Pool jedisPool(RedisProperties properties) {
            return properties.getJedis().getPool();
        }

    }

}

package com.damuzee.boot.spec.tenant.redis;

import com.damuzee.boot.spec.tenant.TenantEnvironment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * redis 配置,注入redis统一key的配置
 */
@Configuration
@EnableConfigurationProperties({RedisKeyProperties.class, CacheProperties.class})
@AutoConfigureBefore({RedisAutoConfiguration.class, RedissonAutoConfiguration.class})
@Slf4j
public class RedisTenantAutoConfiguration extends CachingConfigurerSupport {

    /**
     * redis key 前缀
     */
    @Autowired
    private RedisKeyProperties redisKeyProperties;

    @Resource
    ObjectMapper redisObjectMapper;

    /**
     * 初始化打印
     */
    @PostConstruct
    public void init() {
        log.info("redis[配置],前缀开关={}, key前缀={},key白名单={}",
            this.redisKeyProperties.isEnabled(), this.redisKeyProperties.getPrefix(),
            this.redisKeyProperties.getWhiteList());
    }

    @Bean(name = "customKeyRedisSerializer")
    @ConditionalOnProperty(prefix = "damuzee.redis", value = "enabled", havingValue = "true", matchIfMissing = true)
    public RedisSerializer<String> keyStringRedisSerializer(RedisKeyProperties redisKeyProperties) {
        if (this.redisKeyProperties.isEnabled()) {
            TenantIdHandler h = TenantEnvironment.isGroup() ? new DamuzeeGroupIdHandler() : new DamuzeeTenantIdHandler();
            return new CustomKeyRedisSerializer(h, redisKeyProperties);
        } else {
            return new StringRedisSerializer();
        }
    }

    @Bean
    @ConditionalOnProperty(prefix = "damuzee.redis", value = "enabled", havingValue = "true", matchIfMissing = true)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory,
        RedisSerializer<String> customKeyRedisSerializer) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);
        stringRedisTemplate.setKeySerializer(customKeyRedisSerializer);
        stringRedisTemplate.setHashKeySerializer(customKeyRedisSerializer);
        return stringRedisTemplate;
    }

    @Bean(name = "redisTemplate")
    @ConditionalOnProperty(prefix = "damuzee.redis", value = "enabled", havingValue = "true", matchIfMissing = true)
    public RedisTemplate<String, ?> objectRedisTemplate(RedisConnectionFactory redisConnectionFactory,
        RedisSerializer<String> customKeyRedisSerializer) {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(
            this.redisObjectMapper);

        RedisTemplate<String, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(customKeyRedisSerializer);
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashKeySerializer(customKeyRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    @ConditionalOnProperty(prefix = "damuzee.redis", value = "enabled", havingValue = "true")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory,
        CacheProperties cacheProperties,
        RedisSerializer<String> customKeyRedisSerializer) {

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(
            this.redisObjectMapper);
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        SerializationPair<String> serializationPair = SerializationPair.fromSerializer(
            customKeyRedisSerializer);

        Redis redisProperties = cacheProperties.getRedis();
        if (log.isDebugEnabled()) {
            log.debug("redis[配置],TimeToLive = {}", redisProperties.getTimeToLive());
        }
        RedisCacheConfiguration redisCacheConfiguration = config
            .entryTtl(redisProperties.getTimeToLive())
            // 键序列化方式 redis字符串序列化
            .serializeKeysWith(serializationPair)
            // 值序列化方式 简单json序列化
            .serializeValuesWith(
                SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer));
        RedisCacheManagerBuilder builder = RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(redisCacheConfiguration);
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (log.isDebugEnabled()) {
            log.debug("redis[配置],cacheNames = {}", cacheNames);
        }
        if (!cacheNames.isEmpty()) {
            builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
        }
        if (cacheProperties.getRedis().isEnableStatistics()) {
            builder.enableStatistics();
        }
        if (log.isDebugEnabled()) {
            log.debug("redis[配置],isEnableStatistics = {}", cacheProperties.getRedis().isEnableStatistics());
        }
        return builder.build();

    }

    @Bean
    public TenantLettuceClientConfigurationBuilderCustomizer tenantLettuceClientConfigurationBuilderCustomizer() {
        return new TenantLettuceClientConfigurationBuilderCustomizer();
    }

}

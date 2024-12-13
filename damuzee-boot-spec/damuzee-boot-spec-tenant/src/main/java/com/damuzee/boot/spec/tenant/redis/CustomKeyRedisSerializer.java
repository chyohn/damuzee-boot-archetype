package com.damuzee.boot.spec.tenant.redis;

import com.damuzee.boot.spec.tenant.jdbc.sql.TenantConfigurationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;

/**
 * 统一给redis key增加项目前缀
 *
 *
 * @apiNote 统一给redis key增加项目前缀,便于多项目工程管理区分
 */
@Slf4j
public class CustomKeyRedisSerializer extends StringRedisSerializer {

    private final Charset charset;

    /**
     * redis 配置信息
     */
    private RedisKeyProperties redisKeyProperties = RedisKeyProperties.defaultRedisKeyProperties();

    private TenantIdHandler tenantIdHandler = new DamuzeeTenantIdHandler();

    /**
     * Creates a new {@link StringRedisSerializer} using {@link StandardCharsets#UTF_8 UTF-8}.
     */
    public CustomKeyRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public CustomKeyRedisSerializer(TenantIdHandler tenantIdHandler, RedisKeyProperties redisKeyProperties) {
        this(StandardCharsets.UTF_8);
        this.tenantIdHandler = tenantIdHandler;
        this.redisKeyProperties = redisKeyProperties;
    }

    /**
     * Creates a new {@link StringRedisSerializer} using the given {@link Charset} to encode and decode strings.
     *
     * @param charset must not be {@literal null}.
     */
    public CustomKeyRedisSerializer(Charset charset) {
        super(charset);
        this.charset = charset;
    }

    @Override
    public byte[] serialize(@Nullable String currentKey) {
        String tenantId = tenantIdHandler.getTenantId();
        if (log.isDebugEnabled()) {
            log.debug("redis[serialize],currentKey={},tenantId={}", currentKey, tenantId);
        }
        if (currentKey == null) {
            String errorMessage = "key is not allow null";
            log.error(errorMessage);
            throw new TenantConfigurationException(errorMessage);
        } else {
            if (this.redisKeyProperties.getWhiteList() != null && !this.redisKeyProperties.getWhiteList().isEmpty()) {
                //匹配白名单
                if (this.redisKeyProperties.getWhiteList().contains(currentKey)) {
                    if (log.isDebugEnabled()) {
                        log.debug("redis多租户,命中白名单,key={}", currentKey);
                    }
                    return currentKey.getBytes(charset);
                }
            }
            if (StringUtils.isBlank(tenantId)) {
                String errorMessage = String.format("currentKey=%s,租户ID,不能为空", currentKey);
                log.error(errorMessage);
                throw new TenantConfigurationException(errorMessage);
            }

            RedisProxy proxy = redisKeyProperties.getProxy();

            String finalKey;
            if (RedisProxy.twemproxy.equals(proxy)) {
                finalKey = RedisPrefixKeyUtil.generate4Twemproxy(tenantId, this.redisKeyProperties.getPrefix(), currentKey);
            } else {
                finalKey = RedisPrefixKeyUtil.generate(tenantId, this.redisKeyProperties.getPrefix(), currentKey);
            }

            if (log.isDebugEnabled()) {
                log.debug("redis[serialize],key={}", finalKey);
            }
            return finalKey.getBytes(this.charset);
        }

    }

    @Override
    public Class<?> getTargetType() {
        return String.class;
    }


}

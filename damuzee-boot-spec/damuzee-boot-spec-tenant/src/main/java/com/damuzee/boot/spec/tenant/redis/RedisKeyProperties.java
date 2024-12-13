package com.damuzee.boot.spec.tenant.redis;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis key统一管理定义
 *
 * 
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "damuzee.tenant.redis.key")
public class RedisKeyProperties {

    /**
     * redis key前缀
     */
    private String prefix;

    /**
     * key白名单,如果是列表里的key,不做key处理
     */
    private List<String> whiteList;


    /**
     * 是否开启前缀,true:自动增加前缀(规则:自定义前缀名称+租户id);false:不开启前缀
     */
    private boolean enabled = false;

    /**
     * 默认普通模式
     */
    private RedisProxy proxy = RedisProxy.nonProxy;

    /**
     * 默认数据对象
     */
    public static RedisKeyProperties defaultRedisKeyProperties() {
        RedisKeyProperties redisKeyProperties = new RedisKeyProperties();
        redisKeyProperties.prefix = "";
        redisKeyProperties.whiteList = new ArrayList<>(0);
        return redisKeyProperties;
    }
}

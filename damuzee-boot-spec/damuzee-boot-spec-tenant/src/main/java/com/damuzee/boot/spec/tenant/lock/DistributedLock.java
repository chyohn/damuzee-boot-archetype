package com.damuzee.boot.spec.tenant.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁 (Distributed Lock) 注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 锁的名称
     *
     * @return <code>String</code>
     */
    String name() default "";


    /**
     * 等待锁的最大时间, 默认 5 秒 如果该值<=0,则不等待
     *
     * @return <code>long</code>
     */
    long waitTime() default 5 * 1000;


    /**
     * 等待锁的时间单位, 默认 TimeUnit.MILLISECONDS 毫秒
     *
     * @return <code>long</code>
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 是否在锁名称前面添加租户前缀，默认 false，不添加
     *
     * @return <code>boolean</code>
     */
    boolean tenantPrefix() default false;

    /**
     * 业务编码，在分布式锁获取失败或IO异常发生时原样随异常返回
     *
     * @return <code>String</code>
     * @see DistributedLockException#getBizCode()
     */
    String bizCode() default "";
}

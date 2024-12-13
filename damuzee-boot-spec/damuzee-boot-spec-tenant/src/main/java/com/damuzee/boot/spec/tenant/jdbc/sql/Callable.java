package com.damuzee.boot.spec.tenant.jdbc.sql;

/**
 * 不需要检查异常的callable
 *
 * @param <V>
 */
@FunctionalInterface
public interface Callable<V> {

    V call();
}
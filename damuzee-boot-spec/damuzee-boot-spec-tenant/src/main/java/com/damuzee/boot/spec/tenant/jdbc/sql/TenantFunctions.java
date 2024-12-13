package com.damuzee.boot.spec.tenant.jdbc.sql;

import com.damuzee.boot.spec.tenant.jdbc.interceptor.TenantDynamicTableNameInnerInterceptor;

public class TenantFunctions {

    public static <V> V callWithIgnoreCheck(Callable<V> callable) {
        DamuzeeTenantSqlContext.get().setIgnoreCheck(true);
        V call;
        try {
            call = callable.call();
        } finally {
            DamuzeeTenantSqlContext.clean();
        }
        return call;
    }

    public static void runWithIgnoreCheck(Runnable runnable) {
        DamuzeeTenantSqlContext.get().setIgnoreCheck(true);
        try {
            runnable.run();
        } finally {
            DamuzeeTenantSqlContext.clean();
        }
    }

    /**
     * 不拼接tenant条件并且指定分表
     * <p>
     * 需要配合  {@link TenantDynamicTableNameInnerInterceptor }
     */
    public static <V> V callWithIgnoreCheckAndHintSharding(Callable<V> callable, String shardingValue) {
        DamuzeeTenantSqlContext sqlContext = DamuzeeTenantSqlContext.get();
        sqlContext.setIgnoreCheck(true);
        sqlContext.setShardingValue(shardingValue);
        V call;
        try {
            call = callable.call();
        } finally {
            DamuzeeTenantSqlContext.clean();
        }
        return call;
    }

    /**
     * 不拼接tenant条件并且指定分表
     * <p>
     * 需要配合  {@link TenantDynamicTableNameInnerInterceptor }
     */
    public static void runWithIgnoreCheckAndHintSharding(Runnable runnable, String shardingValue) {
        DamuzeeTenantSqlContext sqlContext = DamuzeeTenantSqlContext.get();
        sqlContext.setIgnoreCheck(true);
        sqlContext.setShardingValue(shardingValue);
        try {
            runnable.run();
        } finally {
            DamuzeeTenantSqlContext.clean();
        }
    }

}

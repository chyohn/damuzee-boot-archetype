package com.damuzee.boot.spec.tenant.jdbc.sql;

import lombok.Data;

import java.util.Objects;

@Data
public class DamuzeeTenantSqlContext {

    /**
     * 是否忽略检查
     */
    private boolean ignoreCheck = false;
    private String shardingValue;

    private static final ThreadLocal<DamuzeeTenantSqlContext> holder = new ThreadLocal<>();

    public static DamuzeeTenantSqlContext get() {
        DamuzeeTenantSqlContext context = holder.get();
        if (Objects.isNull(context)) {
            context = new DamuzeeTenantSqlContext();
            holder.set(context);
        }
        return context;
    }

    public static void set(DamuzeeTenantSqlContext context) {
        holder.set(context);
    }

    public static void clean() {
        holder.remove();
    }

}

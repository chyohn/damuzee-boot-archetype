package com.damuzee.boot.spec.util;

import java.util.concurrent.Callable;
import lombok.SneakyThrows;

/**
 * {@link DamuzeeArchContext} 的工具类
 *
 */
public final class DamuzeeArchContextUtil {

    public static Long getTenantId() {
        final String tenantId = DamuzeeArchContext.TENANT_ID.get();
        if (null == tenantId || tenantId.length() <= 0) {
            return null;
        } else {
            return Long.parseLong(tenantId);
        }
    }

    public static void setTenantId(Long tenantId) {
        DamuzeeArchContext.TENANT_ID.set(tenantId == null ? null : tenantId.toString());
    }

    public static Long getGroupId() {
        final String groupId = DamuzeeArchContext.GROUP_ID.get();
        if (null == groupId || groupId.length() <= 0) {
            return null;
        } else {
            return Long.parseLong(groupId);
        }
    }


    public static void setGroupId(Long groupId) {
        DamuzeeArchContext.GROUP_ID.set(groupId == null ? null : groupId.toString());
    }

    @SneakyThrows
    public static <V> V callWithGroupId(Long newGroupId, Callable<V> callable) {
        Long oldGroupId = getGroupId();
        setGroupId(newGroupId);
        try {
            return callable.call();
        } finally {
            setGroupId(oldGroupId);
        }
    }

    public static void runWithGroupId(Long newGroupId, Runnable runnable) {
        callWithGroupId(newGroupId, () -> {
            runnable.run();
            return true;
        });
    }

    @SneakyThrows
    public static <V> V callWithTenantId(Long newTenantId, Callable<V> callable) {
        Long oldTenantId = getTenantId();
        setTenantId(newTenantId);
        try {
            return callable.call();
        } finally {
            setTenantId(oldTenantId);
        }
    }

    public static void runWithTenantId(Long newTenantId, Runnable runnable) {
        callWithTenantId(newTenantId, () -> {
            runnable.run();
            return true;
        });
    }
}

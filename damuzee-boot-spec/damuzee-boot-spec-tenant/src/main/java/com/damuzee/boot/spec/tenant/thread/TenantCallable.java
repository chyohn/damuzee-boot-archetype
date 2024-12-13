package com.damuzee.boot.spec.tenant.thread;

import com.damuzee.boot.spec.util.DamuzeeArchContext;
import com.damuzee.boot.spec.tenant.TenantEnvironment;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.StringUtils;


public class TenantCallable<V> implements Callable<V> {

    /**
     * 租户ID或集团ID
     */
    private String tenantId;

    private final Callable<V> callable;

    /**
     * used when without dsw agent
     */
    private DamuzeeArchContext.Opaque data;

    public TenantCallable(Callable<V> callable) {
        this.callable = callable;
        this.data = DamuzeeArchContext.capture();
    }

    /**
     *
     * @param tenantId 租户ID，或集团ID
     * @param callable
     */
    public TenantCallable(String tenantId, Callable<V> callable) {
        if (StringUtils.isBlank(tenantId)) {
            throw new IllegalArgumentException("tenantId is blank,please assign it");
        }

        this.tenantId = tenantId;
        this.callable = callable;
        this.data = DamuzeeArchContext.capture();
    }


    @Override
    public V call() throws Exception {
        DamuzeeArchContext.restore(data);
        try {
            if (StringUtils.isNotEmpty(this.tenantId)) {
                DamuzeeArchContext c =
                    TenantEnvironment.isGroup() ? DamuzeeArchContext.GROUP_ID : DamuzeeArchContext.TENANT_ID;
                c.set(this.tenantId);
            }

            return callable.call();
        } finally {
            DamuzeeArchContext.clear();
        }
    }

}

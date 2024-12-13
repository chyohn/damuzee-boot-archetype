package com.damuzee.boot.spec.tenant.thread;

import com.damuzee.boot.spec.util.DamuzeeArchContext;
import com.damuzee.boot.spec.tenant.TenantEnvironment;
import org.apache.commons.lang3.StringUtils;

public class TenantRunnable implements Runnable {
    /**
     * 租户ID或集团ID
     */
    private String tenantId;

    private final Runnable runnable;

    /**
     * used when without dsw agent
     */
    private DamuzeeArchContext.Opaque data;

    public TenantRunnable(Runnable runnable) {
        this.runnable = runnable;
        this.data = DamuzeeArchContext.capture();
    }

    /**
     * @param tenantId 租户ID或集团ID
     * @param runnable
     */
    public TenantRunnable(String tenantId, Runnable runnable) {
        if (StringUtils.isBlank(tenantId)) {
            throw new IllegalArgumentException("tenantId is blank,please assign it");
        }

        this.tenantId = tenantId;
        this.runnable = runnable;
        this.data = DamuzeeArchContext.capture();
    }

    @Override
    public void run() {
        DamuzeeArchContext.restore(this.data);
        try {
            if (StringUtils.isNotEmpty(this.tenantId)) {
                DamuzeeArchContext c =
                        TenantEnvironment.isGroup() ? DamuzeeArchContext.GROUP_ID : DamuzeeArchContext.TENANT_ID;
                c.set(this.tenantId);
//                DamuzeeArchContext.TENANT_ID.set(this.tenantId);
            }

            this.runnable.run();
        } finally {
            DamuzeeArchContext.clear();
        }
    }

}

package com.damuzee.boot.spec.tenant.redis;

import com.damuzee.boot.spec.util.DamuzeeArchContext;

/**
 * 商户ID获取
 *
 * 
 * 
 */
public class DamuzeeTenantIdHandler implements TenantIdHandler {

    @Override
    public String getTenantId() {
        return DamuzeeArchContext.TENANT_ID.get();
    }
}

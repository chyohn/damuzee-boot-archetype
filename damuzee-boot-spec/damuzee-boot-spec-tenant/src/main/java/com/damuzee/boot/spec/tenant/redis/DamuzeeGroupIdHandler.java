package com.damuzee.boot.spec.tenant.redis;


import com.damuzee.boot.spec.util.DamuzeeArchContext;

/**
 * 集团ID获取
 *
 * 
 * 
 */
public class DamuzeeGroupIdHandler implements TenantIdHandler {

    @Override
    public String getTenantId() {
        return DamuzeeArchContext.GROUP_ID.get();
    }
}

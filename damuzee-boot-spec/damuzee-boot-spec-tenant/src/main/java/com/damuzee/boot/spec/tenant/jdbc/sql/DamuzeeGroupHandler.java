package com.damuzee.boot.spec.tenant.jdbc.sql;

import com.damuzee.boot.spec.util.DamuzeeArchContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.StringUtils;

/**
 * 集团处理
 */
public class DamuzeeGroupHandler extends DamuzeeTenantHandler {


    public DamuzeeGroupHandler(DamuzeeTenantProperties properties) {
        super(properties);
    }

    @Override
    public Expression getTenantId() {
        String groupid = DamuzeeArchContext.GROUP_ID.get();
        if (StringUtils.isBlank(groupid)) {
            throw new TenantConfigurationException(
                "the groupId is not exist in context,please check the DamuzeeArchContext.GROUP_ID");
        }

        return new LongValue(groupid);
    }
}

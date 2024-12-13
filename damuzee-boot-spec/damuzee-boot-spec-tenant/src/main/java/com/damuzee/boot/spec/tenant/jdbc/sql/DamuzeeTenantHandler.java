package com.damuzee.boot.spec.tenant.jdbc.sql;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import java.util.List;

import com.damuzee.boot.spec.util.DamuzeeArchContext;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 商户处理
 */
@AllArgsConstructor
public class DamuzeeTenantHandler implements TenantLineHandler {

    protected final DamuzeeTenantProperties properties;

    @Override
    public Expression getTenantId() {
        String tenantId = DamuzeeArchContext.TENANT_ID.get();
        if (StringUtils.isBlank(tenantId)) {
            throw new TenantConfigurationException(
                "the tenantId is not exist in context,please check the DamuzeeArchContext.TENANT_ID");
        }

        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }

    @Override
    public boolean ignoreTable(final String tableName) {
        if (DamuzeeTenantSqlContext.get().isIgnoreCheck()) {
            return true;
        }

        List<String> ignoreTables = properties.getIgnoreTables();
        if (CollectionUtils.isEmpty(ignoreTables)) {
            return false;
        }

        String tableNameTrim = tableName.trim();
        String tableNameNoBackQuote = StringUtils.replace(tableNameTrim, "`", "").trim();

        return ignoreTables.contains(tableNameTrim) || ignoreTables.contains(tableNameNoBackQuote);
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return columns.stream().map(this::resolveColumnName).anyMatch(i -> i.equalsIgnoreCase(tenantIdColumn));
    }

    private String resolveColumnName(Column col) {
        return StringUtils.replace(col.getColumnName(), "`", "").trim();
    }
}

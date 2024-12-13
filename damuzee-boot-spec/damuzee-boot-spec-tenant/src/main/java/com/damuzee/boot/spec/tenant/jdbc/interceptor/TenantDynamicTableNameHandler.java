package com.damuzee.boot.spec.tenant.jdbc.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.damuzee.boot.spec.tenant.jdbc.sql.DamuzeeTenantSqlContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 定制动态表名
 */
@Slf4j
public class TenantDynamicTableNameHandler implements TableNameHandler {

    @Override
    public String dynamicTableName(String sql, String tableName) {
        Optional<String> shardingValue = Optional.ofNullable(DamuzeeTenantSqlContext.get().getShardingValue());
        String resultTable = tableName;
        if (shardingValue.isPresent()) {
            tableName = tableName.replaceAll("`", "");
            resultTable = tableName + "_" + shardingValue.get();
        }
        if (log.isDebugEnabled()) {
            log.debug("TenantDynamicTableNameHandler-sql:{},tableName:{},resultTable:{}", sql, tableName, resultTable);
        }
        return resultTable;
    }

}

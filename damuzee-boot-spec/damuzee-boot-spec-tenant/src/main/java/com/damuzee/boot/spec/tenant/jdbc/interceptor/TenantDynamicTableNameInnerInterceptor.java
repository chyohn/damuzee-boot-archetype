package com.damuzee.boot.spec.tenant.jdbc.interceptor;

import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.TableNameParser;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * copy from {@link com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor}
 * 为了兼容不同版本extension,复制过来定制化改造
 */
@NoArgsConstructor
public class TenantDynamicTableNameInnerInterceptor implements InnerInterceptor {

    @Setter
    private Runnable hook;
    /**
     * 表名处理器，是否处理表名的情况都在该处理器中自行判断 default TenantDynamicTableNameHandler
     */
    @Setter
    private TableNameHandler tableNameHandler = new TenantDynamicTableNameHandler();

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
        ResultHandler resultHandler,
        BoundSql boundSql) throws
        SQLException {
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        if (!InterceptorIgnoreHelper.willIgnoreDynamicTableName(ms.getId())) {
            // 非忽略执行
            mpBs.sql(this.changeTable(mpBs.sql()));
        }
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            if (!InterceptorIgnoreHelper.willIgnoreDynamicTableName(ms.getId())) {
                // 非忽略执行
                PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
                mpBs.sql(this.changeTable(mpBs.sql()));
            }
        }
    }

    protected String changeTable(String sql) {
        if (Objects.isNull(tableNameHandler)) {
            return sql;
        }
        TableNameParser parser = new TableNameParser(sql);
        List<TableNameParser.SqlToken> names = new ArrayList<>();
        parser.accept(names::add);
        StringBuilder builder = new StringBuilder();
        int last = 0;
        for (TableNameParser.SqlToken name : names) {
            int start = name.getStart();
            if (start != last) {
                builder.append(sql, last, start);
                builder.append(tableNameHandler.dynamicTableName(sql, name.getValue()));
            }
            last = name.getEnd();
        }
        if (last != sql.length()) {
            builder.append(sql.substring(last));
        }
        if (hook != null) {
            hook.run();
        }
        return builder.toString();
    }
}

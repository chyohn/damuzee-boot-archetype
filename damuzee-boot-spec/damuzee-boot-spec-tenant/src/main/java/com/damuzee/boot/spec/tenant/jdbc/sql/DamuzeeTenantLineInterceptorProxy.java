package com.damuzee.boot.spec.tenant.jdbc.sql;

import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 扩展租户拦截器的目的是：对SQL中同时添加租户字段和集团字段
 */
class DamuzeeTenantLineInterceptorProxy extends TenantLineInnerInterceptor {

    private final List<TenantLineInnerInterceptor> targets;

    public DamuzeeTenantLineInterceptorProxy(List<TenantLineInnerInterceptor> targets) {
        this.targets = targets;
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        for (TenantLineInnerInterceptor target : targets) {
            target.beforePrepare(sh, connection, transactionTimeout);
        }

    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
        ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        for (TenantLineInnerInterceptor target : targets) {
            target.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
        }
    }
}

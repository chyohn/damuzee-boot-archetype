package com.damuzee.boot.spec.tenant.jdbc.sql;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 扩展租户拦截器的目的是：对于SQL中有租户字段了，就不需要再给表加上租户字段
 */
class DamuzeeTenantLineInnerInterceptor extends TenantLineInnerInterceptor {

    private final TenantLineHandler tenantLineHandler;

    public DamuzeeTenantLineInnerInterceptor(TenantLineHandler tenantLineHandler) {
        this.tenantLineHandler = tenantLineHandler;
        this.setTenantLineHandler(tenantLineHandler);
    }

    /**
     * update 语句处理
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        if (hasTenantColumn(update.getWhere())) {
            return;
        }

        super.processUpdate(update, index, sql, obj);
    }

    /**
     * delete 语句处理
     */
    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        if (hasTenantColumn(delete.getWhere())) {
            return;
        }
        super.processDelete(delete, index, sql, obj);
    }

    /**
     * 处理条件
     *
     * @param currentExpression
     * @param table
     * @return
     */
    @Override
    protected Expression builderExpression(Expression currentExpression, Table table) {
        if (hasTenantColumn(currentExpression)) {
            return currentExpression;
        }
        return super.builderExpression(currentExpression, table);
    }

    /**
     * 判断表达式中是否有租户字段
     *
     * @param expression
     * @return true: 有租户字段
     */
    private boolean hasTenantColumn(Expression expression) {
        if (expression == null) {
            return false;
        }

        TenantColumnVisitor visitor = new TenantColumnVisitor(tenantLineHandler);
        expression.accept(visitor);
        return visitor.hasTenantColumn;
    }

    /**
     * 用于访问列表达式是否有租户
     */
    static class TenantColumnVisitor extends ExpressionVisitorAdapter {

        final TenantLineHandler tenantHandler;
        boolean hasTenantColumn;

        TenantColumnVisitor(TenantLineHandler tenantHandler) {
            this.tenantHandler = tenantHandler;
        }

        @Override
        public void visit(EqualsTo expr) {
            Expression left = expr.getLeftExpression();
            Expression right = expr.getRightExpression();
            if (left instanceof Column && !(right instanceof Column)) {
                chekHasTenantColumn((Column) left);
            }
            if (right instanceof Column && !(left instanceof Column)) {
                chekHasTenantColumn((Column) right);
            }
            super.visit(expr);
        }

        @Override
        public void visit(InExpression expr) {
            Expression left = expr.getLeftExpression();
            if (left instanceof Column) {
                chekHasTenantColumn((Column) left);
            }
            super.visit(expr);
        }

        public void chekHasTenantColumn(Column column) {
            if (column.getColumnName().equalsIgnoreCase(tenantHandler.getTenantIdColumn())) {
                hasTenantColumn = true;
            }
        }
    }

}

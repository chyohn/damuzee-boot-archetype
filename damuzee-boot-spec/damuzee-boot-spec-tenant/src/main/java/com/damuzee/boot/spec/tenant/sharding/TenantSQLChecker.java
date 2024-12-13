package com.damuzee.boot.spec.tenant.sharding;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.infra.executor.check.SQLCheckResult;
import org.apache.shardingsphere.infra.executor.check.SQLChecker;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.metadata.user.Grantee;
import org.apache.shardingsphere.sharding.route.engine.condition.Column;
import org.apache.shardingsphere.sharding.route.engine.condition.generator.ConditionValueGeneratorFactory;
import org.apache.shardingsphere.sharding.route.engine.condition.value.ListShardingConditionValue;
import org.apache.shardingsphere.sharding.route.engine.condition.value.ShardingConditionValue;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.BinaryOperationExpression;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.ExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.simple.LiteralExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.predicate.AndPredicate;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.predicate.WhereSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.TableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.common.util.ColumnExtractor;
import org.apache.shardingsphere.sql.parser.sql.common.util.ExpressionExtractUtil;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.mysql.dml.MySQLSelectStatement;

@Slf4j
public class TenantSQLChecker implements SQLChecker<ShardingRule> {

    private Field field;

    public TenantSQLChecker() {
        try {
            field = LiteralExpressionSegment.class.getDeclaredField("literals");
            field.setAccessible(true);
        } catch (Exception e) {
            log.error("TenantSQLChecker error", e);
        }
    }

    @Override
    public boolean check(String schemaName, Grantee grantee, ShardingRule rule) {
        return true;
    }

    @Override
    public SQLCheckResult check(SQLStatement sqlStatement, List<Object> parameters, Grantee grantee,
        String currentSchema,
        Map<String, ShardingSphereDatabase> metaDataMap, ShardingRule rule) {

        SQLCheckResult result = new SQLCheckResult(true, "");

        try {
            if (!(sqlStatement instanceof MySQLSelectStatement)) {
                //非select的,过滤掉
                return result;
            }

            MySQLSelectStatement mySQLSelectStatement = (MySQLSelectStatement) sqlStatement;
            Optional<WhereSegment> where = mySQLSelectStatement.getWhere();
            //获取表名
            TableSegment tableSegment = mySQLSelectStatement.getFrom();
            if (!(tableSegment instanceof SimpleTableSegment)) {
                return result;
            }
            SimpleTableSegment simpleTableSegment = (SimpleTableSegment) tableSegment;
            String tableName = simpleTableSegment.getTableName().getIdentifier().getValue();
            if (StringUtils.isBlank(tableName)) {
                return result;
            }

            if (!where.isPresent()) {
                return result;
            }

            WhereSegment whereSegment = where.get();

            ExpressionSegment expr = whereSegment.getExpr();
            if (!(expr instanceof BinaryOperationExpression)) {
                return result;
            }
            BinaryOperationExpression binaryOperationExpression = (BinaryOperationExpression) expr;

            Collection<AndPredicate> andPredicates = ExpressionExtractUtil.getAndPredicates(binaryOperationExpression);
            if (andPredicates.size() > 1) {
                return result;
            }
            for (AndPredicate andPredicate : andPredicates) {
                Collection<ExpressionSegment> predicates = andPredicate.getPredicates();
                Type type = extractShardingConditionType(predicates, parameters, tableName, rule);
                if (Objects.isNull(type)) {
                    return result;
                }

                ExpressionSegment right = binaryOperationExpression.getRight();
                if (!(right instanceof BinaryOperationExpression)) {
                    return result;
                }

                BinaryOperationExpression rightBinaryOperationExpression = (BinaryOperationExpression) right;
                //只处理结尾为_tenant_id = xx 的情况
                String text = rightBinaryOperationExpression.getText();
                if (StringUtils.isBlank(text) || !text.contains("_tenant_id")) {
                    return result;
                }

                ExpressionSegment expressionRight = rightBinaryOperationExpression.getRight();
                if (!(expressionRight instanceof LiteralExpressionSegment)) {
                    //LiteralExpressionSegment为具体参数,不是具体参数的比如 ? 直接过滤掉
                    return result;
                }

                LiteralExpressionSegment literalExpressionSegment = (LiteralExpressionSegment) expressionRight;
                //修改right类型为type
                String strValue = literalExpressionSegment.getLiterals().toString();
                if (Integer.class.equals(type)) {
                    field.set(literalExpressionSegment, Integer.parseInt(strValue));
                } else if (Long.class.equals(type)) {
                    field.set(literalExpressionSegment, Long.parseLong(strValue));
                } else if (String.class.equals(type)) {
                    field.set(literalExpressionSegment, strValue);
                }
                //其他类型暂时不管

                //只处理一个
                return result;
            }
        } catch (Throwable e) {
            log.error("TenantSQLChecker error", e);
        }
        return result;
    }

    private Type extractShardingConditionType(final Collection<ExpressionSegment> predicates,
        final List<Object> parameters,
        final String tableName,
        final ShardingRule shardingRule) {
        Map<Column, Collection<ShardingConditionValue>> shardingConditionValueMap = createShardingConditionValueMap(
            predicates, parameters,
            tableName, shardingRule);
        if (MapUtils.isEmpty(shardingConditionValueMap)) {
            return null;
        }

        Collection<Collection<ShardingConditionValue>> values = shardingConditionValueMap.values();
        if (Objects.isNull(values) || values.size() > 1) {
            return null;
        }

        Type type = null;
        for (Collection<ShardingConditionValue> value : values) {
            for (ShardingConditionValue shardingConditionValue : value) {
                if (!(shardingConditionValue instanceof ListShardingConditionValue)) {
                    return null;
                }
                ListShardingConditionValue listShardingConditionValue = (ListShardingConditionValue) shardingConditionValue;
                Collection shardingValues = listShardingConditionValue.getValues();
                if (shardingValues.size() > 1) {
                    return null;
                }
                for (Object shardingValue : shardingValues) {
                    Type currentType = shardingValue.getClass();
                    if (type == null) {
                        type = currentType;
                    }
                }
            }
        }
        return type;
    }

    private Map<Column, Collection<ShardingConditionValue>> createShardingConditionValueMap(
        final Collection<ExpressionSegment> predicates,
        final List<Object> parameters,
        final String tableName,
        final ShardingRule shardingRule) {
        Map<Column, Collection<ShardingConditionValue>> result = new HashMap<>(predicates.size(), 1);
        for (ExpressionSegment each : predicates) {
            for (ColumnSegment columnSegment : ColumnExtractor.extract(each)) {
                Optional<String> shardingColumn = shardingRule
                    .findShardingColumn(columnSegment.getIdentifier().getValue(), tableName);

                if (!shardingColumn.isPresent()) {
                    continue;
                }
                Column column = new Column(shardingColumn.get(), tableName);
                Optional<ShardingConditionValue> shardingConditionValue = ConditionValueGeneratorFactory
                    .generate(each, column, parameters);
                if (!shardingConditionValue.isPresent()) {
                    continue;
                }
                result.computeIfAbsent(column, unused -> new LinkedList<>()).add(shardingConditionValue.get());
            }
        }
        return result;
    }

    @Override
    public boolean check(Grantee grantee, ShardingRule rule) {
        return true;
    }

    @Override
    public boolean check(Grantee grantee, BiPredicate<Object, Object> validator, Object cipher, ShardingRule rule) {
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Class<ShardingRule> getTypeClass() {
        return ShardingRule.class;
    }

}

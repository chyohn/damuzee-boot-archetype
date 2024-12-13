#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.infra.db.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

/**
 * localDate 字段类型支持
 *
 * 
 */
@Component
@MappedTypes(LocalDate.class)
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, LocalDate localDate,
        JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(i, localDate);
    }

    @Override
    public LocalDate getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String target = resultSet.getString(columnName);
        if (target == null || "".equals(target)) {

            return null;
        }
        return LocalDate.parse(target, dateTimeFormatter);
    }

    @Override
    public LocalDate getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String target = resultSet.getString(columnIndex);
        if (target == null || "".equals(target)) {

            return null;
        }
        return LocalDate.parse(target, dateTimeFormatter);
    }

    @Override
    public LocalDate getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String target = callableStatement.getString(columnIndex);
        if (target == null || "".equals(target)) {

            return null;
        }
        return LocalDate.parse(target, dateTimeFormatter);
    }
}

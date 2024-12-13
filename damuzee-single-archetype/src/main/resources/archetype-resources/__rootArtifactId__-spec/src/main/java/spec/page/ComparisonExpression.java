#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.spec.page;

import lombok.Data;

/**
 * 筛选字段
 */
@Data
public class ComparisonExpression {


    /**
     * 字段
     */
    private String column;

    /**
     * 筛选方式 ,比如: "=",">"
     */
    private String operator;

    /**
     * 字段值
     */
    private Object value;

}


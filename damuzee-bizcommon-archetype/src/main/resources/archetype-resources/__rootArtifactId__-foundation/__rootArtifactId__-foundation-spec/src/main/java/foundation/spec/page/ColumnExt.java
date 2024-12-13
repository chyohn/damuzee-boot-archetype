#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.foundation.spec.page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnExt {

    /**
     * 列固定设置
     */
    private String fixed;
    /**
     * 是否可见
     */
    private boolean visible;
    /**
     * 列的副标题
     */
    private String subLabel;
    /**
     * 列宽
     */
    private String width;
    /**
     * 列宽最小值
     */
    private String minWidth;
    /**
     * 是否允许自定义
     */
    private boolean settable;
    /**
     * 对齐 'left' | 'center' | 'right'
     */
    private String align;

    /**
     * 组件格式化类型:日期（dateTime）、数量（quantity)、单价(unitPrice)、金额(amount)
     */
    private String formatter;

    /**
     * 字段提示信息
     */
    private String tips;

    /**
     * 超长则自动省略号隐藏,hover提示
     */
    private Boolean ellipsis;


}

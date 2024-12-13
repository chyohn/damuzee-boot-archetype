#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.spec.page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnExt {

    private String fixed;//列固定设置
    private boolean visible; // 是否可见
    private String subLabel;  //列的副标题
    private String width;  //列宽
    private String minWidth; //列宽最小值
    private boolean settable; //是否允许自定义
    private String align;


}

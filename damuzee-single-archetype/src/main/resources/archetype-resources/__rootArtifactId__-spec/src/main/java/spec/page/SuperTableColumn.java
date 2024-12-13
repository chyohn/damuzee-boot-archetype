#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.spec.page;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuperTableColumn {

    private String label; // 列的标题
    private String name; // 列字段名称
    private boolean sortable; //是否开启列排序
    private String tips; // 列提示
    private List<SuperTableColumn> children = Collections.emptyList();


    // 列扩展信息
    private ColumnExt ext;


}

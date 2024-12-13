#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.foundation.spec.page;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuperTableColumn {

    /**
     * 列的标题
     */
    private String label;
    /**
     * 列字段名称
     */
    private String name;
    /**
     * 是否开启列排序
     */
    private boolean sortable;
    /**
     * 列提示
     */
    private String tips;

    /**
     * 列扩展信息
     */
    private ColumnExt ext;


    private List<SuperTableColumn> children = Collections.emptyList();


    public SuperTableColumn(String label, String name, boolean sortable) {
        this.label = label;
        this.name = name;
        this.sortable = sortable;
    }

    public SuperTableColumn(String label, String name) {
        this.label = label;
        this.name = name;
        this.sortable = false;
    }


    public SuperTableColumn() {
    }


}

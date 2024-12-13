#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.spec.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class SuperPage<T> extends Page<T> {

    @Getter
    @Setter
    private List<ComparisonExpression> filters = new ArrayList<>();

    @Getter
    @Setter
    private List<SuperTableColumn> columns = new ArrayList<>();

    /**
     * @param current 当前页
     * @param size    每页显示条数
     */
    public SuperPage(long current, long size) {
        super(current, size);
    }

    /**
     * @param pageNo   当前页
     * @param pageSize 页大小
     * @param total    总条数
     */
    public SuperPage(long pageNo, long pageSize, long total) {
        super(pageNo, pageSize, total);
    }

}

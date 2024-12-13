package com.damuzee.boot.spec.tenant.jdbc.sql;

import com.damuzee.boot.spec.tenant.TenantEnvironment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mybatis-plus mysql租户插件
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "damuzee.tenant.db")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DamuzeeTenantProperties {

    /**
     * 多租户字段名称
     * <p>默认 _tenant_id或_group_id</p>
     */
    private String column;

    /**
     * 忽略的表
     */
    private List<String> ignoreTables;

    /**
     * 默认开启
     */
    private boolean enabled = true;

    /**
     * 集团配置，有的租户环境需要
     */
    private DamuzeeGroupProperties group;


    public String getColumn() {
        if (StringUtils.isNotBlank(column)) {
            return column;
        }
        if (TenantEnvironment.isGroup()) {
            column = "_group_id";
        } else {
            column = "_tenant_id";
        }
        return column;
    }

    @Getter
    @Setter
    static class DamuzeeGroupProperties {

        /**
         * 多租户字段名称
         * <p>默认 _tenant_id或_group_id</p>
         */
        private String column;

        /**
         * 忽略的表
         */
        private List<String> ignoreTables;

        public String getColumn() {
            if (StringUtils.isNotBlank(column)) {
                return column;
            }

            column = "_group_id";
            return column;
        }
    }
}

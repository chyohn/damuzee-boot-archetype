#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
package ${package}.foundation.infra.db.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Map;

/**
 * TODO 动态数据源配置,不需要的直接删除该类
 */
@Configuration
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true")
public class DataSourceConfiguration {

    public static final String SHARDING = "shardingSphereDataSource";

    @Autowired
    private DynamicDataSourceProperties properties;

    @Autowired(required = false)
    @Qualifier(SHARDING)
    private DataSource shardingSphereDataSource;

    /**
     * 加入 shardingSphere 的数据源。
     */
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        if (shardingSphereDataSource == null) {
            return new YmlDynamicDataSourceProvider(properties.getDatasource());
        }
        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourceMap = createDataSourceMap(properties.getDatasource());
                dataSourceMap.put(SHARDING, shardingSphereDataSource);
                return dataSourceMap;
            }
        };
    }

    /**
     * 设置主数据源。
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }
}

package com.damuzee.boot.spec.tenant.jdbc.sql;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.damuzee.boot.spec.tenant.TenantEnvironment;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(DamuzeeTenantProperties.class)
@ConditionalOnProperty(prefix = "damuzee.tenant.db", value = "enabled", havingValue = "true", matchIfMissing = true)
public class MybatisTenantAutoConfiguration {

    @Bean
    @Primary
    public TenantLineHandler tenantHandler(DamuzeeTenantProperties tenantProperties) {

        if (TenantEnvironment.isGroup()) {
            return new DamuzeeGroupHandler(tenantProperties);
        }

        return new DamuzeeTenantHandler(tenantProperties);
    }

    @Bean
    @Primary
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantLineHandler tenantHandler, DamuzeeTenantProperties tenantProperties) {

        TenantLineInnerInterceptor tenantInterceptor = new DamuzeeTenantLineInnerInterceptor(tenantHandler);
        DamuzeeTenantProperties.DamuzeeGroupProperties groupConfig = tenantProperties.getGroup();
        if (groupConfig == null) {
            return tenantInterceptor;
        }

        if (TenantEnvironment.isGroup()) {
            throw new IllegalArgumentException("集团端不需要配置damuzee.tenant.db.group");
        }

        // 配置集团拦截器
        // 创建集团配置
        DamuzeeTenantProperties groupProperties = DamuzeeTenantProperties.builder()
            .ignoreTables(groupConfig.getIgnoreTables())
            .column(groupConfig.getColumn())
            .enabled(true)
            .build();
        // 创建集团处理器
        DamuzeeGroupHandler groupHandler = new DamuzeeGroupHandler(groupProperties);
        // 创建集团拦截器
        TenantLineInnerInterceptor groupInterceptor = new DamuzeeTenantLineInnerInterceptor(groupHandler);

        return new DamuzeeTenantLineInterceptorProxy(Lists.newArrayList(tenantInterceptor, groupInterceptor));
    }

}

package com.damuzee.boot.spec.env.config;

import com.damuzee.boot.spec.env.EnvUtils;
import com.damuzee.boot.spec.env.property.EnvironmentProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
public class EnvironmentAutoConfiguration {

    @Bean
    public EnvironmentProperties environmentProperties() {
        EnvironmentProperties properties = new EnvironmentProperties();
        // 设置到工具类中
        EnvUtils.setEnvPros(properties);
        return properties;
    }


}

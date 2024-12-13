package com.damuzee.boot.spec.jackson.spring;

import com.damuzee.boot.spec.jackson.utils.JSONUtil;
import com.damuzee.boot.spec.jackson.serializer.JacksonBuilder;
import com.damuzee.boot.spec.jackson.serializer.LongToStringSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties({JacksonProperties.class})
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class DamuzeeJacksonAutoConfiguration {

    @Autowired
    private JacksonProperties jacksonProperties;

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JacksonBuilder.builder(jacksonProperties)
            .addSerializer(new LongToStringSerializer())
            .build();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public JSONUtil jsonUtil() {
        ObjectMapper objectMapper = JacksonBuilder.builder(jacksonProperties).build();
        ObjectMapper objectMapperWriteNull = JacksonBuilder.builder(jacksonProperties).writeNull().build();
        ObjectMapper genericObjectMapper = JacksonBuilder.builder(jacksonProperties).withJavaType().build();
        JSONUtil.setObjectMapper(objectMapper, objectMapperWriteNull, genericObjectMapper);
        return new JSONUtil();
    }

    @Bean
    @ConditionalOnMissingBean
    public InnerDateTimeInitBinder innerDateTimeInitBinder() {
        return new InnerDateTimeInitBinder(jacksonProperties);
    }

    //======================================================================
    //  用于 Redis 处理的 ObjectMapper
    //======================================================================
    @Bean(name = "redisObjectMapper")
    public ObjectMapper redisObjectMapper() {
        return JacksonBuilder.builder(jacksonProperties).withJavaType().build();
    }

}

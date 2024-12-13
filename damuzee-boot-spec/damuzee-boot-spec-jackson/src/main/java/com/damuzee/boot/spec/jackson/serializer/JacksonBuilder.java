package com.damuzee.boot.spec.jackson.serializer;

import com.damuzee.boot.spec.jackson.spring.JacksonProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JacksonBuilder {

    private ObjectMapper objectMapper;
    private final JacksonProperties jacksonProperties;
    private final SimpleModule customModule;

    private JacksonBuilder() {
        this(new JacksonProperties());
    }

    private JacksonBuilder(JacksonProperties jacksonProperties) {
        this.jacksonProperties = jacksonProperties;
        customModule = new SimpleModule();
        //default
        defaultObjectMapper();
    }

    private void defaultObjectMapper() {
        //处理key为null情况 todo 预留暂不开放
        DefaultSerializerProvider provider = new DefaultSerializerProvider.Impl();
        //noinspection unchecked
        provider.setNullKeySerializer(new NullKeySerializer());

        //处理key number类型不匹配
        customModule.addKeySerializer(Integer.class, new FixedNumberKeySerializer(5, Integer.class));
        customModule.addKeySerializer(Long.class, new FixedNumberKeySerializer(6, Integer.class));

        //default
        objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
            .registerModule(javaTimeModule())
            .registerModule(new Jdk8Module());
        String utilDateFormat = jacksonProperties.getUtilDateFormat();
        if (utilDateFormat != null && utilDateFormat.length() > 0) {
            objectMapper.setDateFormat(new SimpleDateFormat(utilDateFormat));
        }

        if (this.jacksonProperties.getDefaultPropertyInclusion() != null) {
            objectMapper.setSerializationInclusion(this.jacksonProperties.getDefaultPropertyInclusion());
        }
    }

    private Module javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();

        module.addSerializer(localDateTimeSerializer());
        module.addSerializer(localDateSerializer());
        module.addSerializer(localTimeSerializer());

        module.addDeserializer(LocalDateTime.class, fixedLocalDateTimeDeserializer());
        module.addDeserializer(LocalDate.class, localDateDeserializer());
        module.addDeserializer(LocalTime.class, localTimeDeserializer());
        return module;
    }

    private LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(jacksonProperties.getDateTimeFormat()));
    }

    private FixedLocalDateTimeDeserializer fixedLocalDateTimeDeserializer() {
        return new FixedLocalDateTimeDeserializer(DateTimeFormatter.ofPattern(jacksonProperties.getDateTimeFormat()));
    }

    private LocalDateSerializer localDateSerializer() {
        return new LocalDateSerializer(DateTimeFormatter.ofPattern(jacksonProperties.getDateFormat()));
    }

    private LocalTimeSerializer localTimeSerializer() {
        return new LocalTimeSerializer(DateTimeFormatter.ofPattern(jacksonProperties.getTimeFormat()));
    }

    private LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(jacksonProperties.getDateTimeFormat()));
    }

    private LocalDateDeserializer localDateDeserializer() {
        return new LocalDateDeserializer(DateTimeFormatter.ofPattern(jacksonProperties.getDateFormat()));
    }

    private LocalTimeDeserializer localTimeDeserializer() {
        return new LocalTimeDeserializer(DateTimeFormatter.ofPattern(jacksonProperties.getTimeFormat()));
    }

    public JacksonBuilder writeNull() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return this;
    }

    public JacksonBuilder withJavaType() {
        objectMapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return this;
    }

    public JacksonBuilder addSerializer(JsonSerializer<?> ser) {
        customModule.addSerializer(ser);
        return this;
    }

    public ObjectMapper build() {
        objectMapper.registerModule(customModule);
        return objectMapper;
    }

    public static JacksonBuilder builder() {
        return new JacksonBuilder();
    }

    public static JacksonBuilder builder(JacksonProperties jacksonProperties) {
        return new JacksonBuilder(jacksonProperties);
    }

}

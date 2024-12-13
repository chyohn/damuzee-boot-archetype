package com.damuzee.boot.spec.jackson.utils;

import com.damuzee.boot.spec.jackson.utils.fastjson.JSONException;
import com.damuzee.boot.spec.jackson.exception.JacksonIOException;
import com.damuzee.boot.spec.jackson.serializer.JacksonBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JSONUtil {

    /**
     * 不带类型的
     */
    protected volatile static ObjectMapper staticObjectMapper;
    /**
     * 不带类型的-null也序列化
     */
    protected volatile static ObjectMapper staticObjectMapperWitheNull;
    /**
     * 带Java类型的json mapper
     */
    protected volatile static ObjectMapper staticGenericObjectMapper;

    static {
        //未被spring注入之前,使用默认mapper
        ObjectMapper defaultObjectMapper = JacksonBuilder.builder().build();
        ObjectMapper defaultObjectMapperWriteNull = JacksonBuilder.builder().writeNull().build();
        ObjectMapper defaultTypeObjectMapper = JacksonBuilder.builder().withJavaType().build();

        setObjectMapper(defaultObjectMapper, defaultObjectMapperWriteNull, defaultTypeObjectMapper);
    }

    /**
     * 验证text是否为空
     *
     * @param text
     * @return true text为空或全为空字符串
     */
    protected static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

//    public JSONUtil(ObjectMapper objectMapper, ObjectMapper objectMapperWriteNull, ObjectMapper genericObjectMapper) {
//        setObjectMapper(objectMapper, objectMapperWriteNull, genericObjectMapper);
//    }

    public static void setObjectMapper(ObjectMapper objectMapper, ObjectMapper defaultObjectMapperWriteNull,
        ObjectMapper redisObjectMapper) {
        if (Objects.nonNull(objectMapper)) {
            staticObjectMapper = objectMapper;
        }
        if (Objects.nonNull(defaultObjectMapperWriteNull)) {
            staticObjectMapperWitheNull = defaultObjectMapperWriteNull;
        }
        if (Objects.nonNull(redisObjectMapper)) {
            staticGenericObjectMapper = redisObjectMapper;
        }
    }

    public static <T> String toJSONString(T obj) {
        try {
            if (obj == null) {
                return null;
            }
            return staticObjectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JacksonIOException(e);
        }
    }

    public static String toJSONString(Object object, boolean prettyFormat) {
        if (object == null) {
            return null;
        }
        if (!prettyFormat) {
            return toJSONString(object);
        }
        try {
            return staticObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new JSONException("can not cast to JSON string.", e);
        }
    }

    public static <T> String toJSONStringWithNull(T obj) {
        try {
            if (obj == null) {
                return null;
            }
            return staticObjectMapperWitheNull.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JacksonIOException(e);
        }
    }

    /**
     * `````````
     *
     * @param json
     * @return
     */
    public static ObjectNode parseObjectNode(String json) {
        try {
            if (isEmpty(json)) {
                return null;
            }

            return staticObjectMapper.readValue(json, ObjectNode.class);
        } catch (IOException e) {
            throw new JacksonIOException(e);
        }
    }

    public static ObjectNode parseObjectNode(Object obj) {
        if (obj == null) {
            return null;
        }
        return staticObjectMapper.valueToTree(obj);
    }


    public static <T> T parseObject(String json, Class<T> clz) {
        try {
            if (clz == null) {
                throw new IllegalArgumentException("json or clz is NULL.");
            }
            if (isEmpty(json)) {
                return null;
            }

            return staticObjectMapper.readValue(json, clz);
        } catch (IOException e) {
            throw new JacksonIOException(e);
        }
    }

    public static <T> T parseObject(JsonNode node, TypeReference<T> valueTypeRef) {
        if (node == null) {
            return null;
        }
        return parseObject(node.toString(), valueTypeRef);
    }

    public static <T> T parseObject(String content, TypeReference<T> valueTypeRef) {
        try {
            if (isEmpty(content)) {
                return null;
            }
            return staticObjectMapper.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error(
                "deserialize to object fail, please check the input param, content:{}, valueTypeRef:{}",
                content, valueTypeRef, e);
            throw new IllegalArgumentException(
                "deserialize to object fail, please check the input String param", e);
        }
    }

    public static <T> T parseObject(Object obj, Class<T> clazz) {
        try {
            if (obj == null) {
                return null;
            }
            String objStr = staticObjectMapper.writeValueAsString(obj);
            return staticObjectMapper.readValue(objStr, clazz);
        } catch (Exception e) {
            throw new JSONException("can not cast to Object.", e);
        }
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (isEmpty(text)) {
            return null;
        }

        JavaType javaType = staticObjectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return staticObjectMapper.readValue(text, javaType);
        } catch (JsonProcessingException e) {
            log.error("parseArray error", e);
            throw new JacksonIOException(e.getMessage());
        }
    }

    /**
     * <pre>
     * 输出的json中带有Java类型，方便再json转对象时能找到具体的实现类。
     * 通常用于对接口和抽象类的实现类对象再JSON字符串之间转换
     * 另见反序列化方法：{@link #parseObjectGeneric(String, Class)}
     * </pre>
     *
     * @param obj
     * @param <T>
     * @return 输出的json中带有Java类型
     */
    public static <T> String toJSONStringGeneric(T obj) {
        try {
            if (obj == null) {
                return null;
            }
            return staticGenericObjectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JacksonIOException(e);
        }
    }

    /**
     * <pre>
     * 把带类型的json字符串还原为java类型
     * 另见反序列化方法：{@link #parseObjectGeneric(String, Class)}
     * </pre>
     *
     * @param json json中带有Java类型
     * @return
     */
    public static Object parseObjectGeneric(String json) {
        return parseObjectGeneric(json, Object.class);
    }

    /**
     * <pre>
     * 把带类型的json字符串还原为指定的java类型
     * 另见序列化方法：{@link #toJSONStringGeneric(Object)}
     * </pre>
     *
     * @param json json中带有Java类型
     * @return
     */
    public static <T> T parseObjectGeneric(String json, Class<T> clz) {
        try {
            if (clz == null) {
                throw new IllegalArgumentException("json or clz is NULL.");
            }
            if (isEmpty(json)) {
                return null;
            }

            return staticGenericObjectMapper.readValue(json, clz);
        } catch (IOException e) {
            throw new JacksonIOException(e);
        }
    }

    public static boolean isValid(String str) {
        if (isEmpty(str)) {
            return false;
        }

        try {
            staticObjectMapper.readTree(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidObject(String str) {
        if (isEmpty(str)) {
            return false;
        }

        try {
            JsonNode node = staticObjectMapper.readTree(str);
            return node.isObject();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidArray(String str) {
        if (isEmpty(str)) {
            return false;
        }

        try {
            JsonNode node = staticObjectMapper.readTree(str);
            return node.isArray();
        } catch (Exception e) {
            return false;
        }
    }

    public static ObjectNode createJsonNode() {
        return staticObjectMapper.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return staticObjectMapper.createArrayNode();
    }

//    public static JsonNode parseObject2Node(Object o) {
//        return staticObjectMapper.valueToTree(o);
//    }

}

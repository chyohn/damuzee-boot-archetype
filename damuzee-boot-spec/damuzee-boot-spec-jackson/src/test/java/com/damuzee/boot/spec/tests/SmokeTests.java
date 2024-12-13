package com.damuzee.boot.spec.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.damuzee.boot.spec.jackson.spring.JacksonProperties;
import com.damuzee.boot.spec.jackson.utils.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {JSONUtil.class, JacksonProperties.class})
public class SmokeTests {

    @Autowired
    JSONUtil jSONUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Resource
    ObjectMapper redisObjectMapper;


    @Test
    public void contextLoads() {
        assertThat(jSONUtil).isNotNull();
        assertThat(objectMapper).isNotNull();
    }

    @Test
    public void testObjectMapperToJson() throws JsonProcessingException {
        LocalDateTime time = LocalDateTime.of(2022, 3, 1, 0, 0, 0);
        AdjustBillModel adjustBillModel = new AdjustBillModel();
        adjustBillModel.setCreated(time);
        adjustBillModel.setModified(time);
        String json = objectMapper.writeValueAsString(adjustBillModel);
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo("{\"modified\":\"2022-03-01 00:00:00\",\"created\":\"2022-03-01 00:00:00\"}");
    }

    @Test
    public void testJacksonMapperParseObject() {
        LocalDateTime exceptedTime = LocalDateTime.of(2022, 3, 1, 0, 0, 0);
        String json = "{\"modified\":\"2022-03-01 00:00:00\",\"created\":\"2022-03-01 00:00:00\"}";
        AdjustBillModel model = JSONUtil.parseObject(json, AdjustBillModel.class);
        assertThat(model).isNotNull();
        assertThat(model.getCreated()).isEqualTo(exceptedTime);
        assertThat(model.getModified()).isEqualTo(exceptedTime);
    }


    @Test
    public void testJacksonMapperParseObjectIncludeUnknownField() {
        LocalDateTime exceptedTime = LocalDateTime.of(2022, 3, 1, 0, 0, 0);
        String json = "{\"modified\":\"2022-03-01 00:00:00\",\"created\":\"2022-03-01 00:00:00\",\"unknownField\":\"unknownField\"}";
        AdjustBillModel model = JSONUtil.parseObject(json, AdjustBillModel.class);
        assertThat(model).isNotNull();
        assertThat(model.getCreated()).isEqualTo(exceptedTime);
        assertThat(model.getModified()).isEqualTo(exceptedTime);
    }

    @Test
    public void testJacksonMapperToJson() {
        LocalDateTime time = LocalDateTime.of(2022, 3, 1, 0, 0, 0);
        AdjustBillModel adjustBillModel = new AdjustBillModel();
        adjustBillModel.setCreated(time);
        adjustBillModel.setModified(time);
        String json = JSONUtil.toJSONString(adjustBillModel);
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo("{\"modified\":\"2022-03-01 00:00:00\",\"created\":\"2022-03-01 00:00:00\"}");
    }
//
//    @Test
//    public void testGenericJackson2JsonRedisSerializer() {
//        LocalDateTime time = LocalDateTime.of(2022, 3, 1, 0, 0, 0);
//        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(
//            redisObjectMapper);
//        AdjustBillModel adjustBillModel = new AdjustBillModel();
//        adjustBillModel.setCreated(time);
//        adjustBillModel.setModified(time);
//
//        byte[] bytes = genericJackson2JsonRedisSerializer.serialize(adjustBillModel);
//
//        AdjustBillModel model = genericJackson2JsonRedisSerializer.deserialize(bytes, AdjustBillModel.class);
//
//        assertThat(bytes).isNotEmpty();
//        assertThat(model).isNotNull();
//    }


    @Test
    public void testGenericJackson2JsonRedisSerializerIncludeType() throws JsonProcessingException {
        LocalDateTime time = LocalDateTime.of(2022, 3, 1, 0, 0, 0);
        AdjustBillModel adjustBillModel = new AdjustBillModel();
        adjustBillModel.setCreated(time);
        adjustBillModel.setModified(time);
        String json = redisObjectMapper.writeValueAsString(adjustBillModel);
        System.out.println(redisObjectMapper);
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(
            "{\"@class\":\"tests.com.damuzee.boot.spec.AdjustBillModel\",\"modified\":\"2022-03-01 00:00:00\",\"created\":\"2022-03-01 00:00:00\"}");
    }

}

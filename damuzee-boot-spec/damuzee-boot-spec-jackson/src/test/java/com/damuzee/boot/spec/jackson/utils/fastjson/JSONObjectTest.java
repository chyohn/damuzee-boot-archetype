package com.damuzee.boot.spec.jackson.utils.fastjson;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 */
class JSONObjectTest {

    static JSONObject jsonObject = null;

    @Data
    static class Test1 {

        private int id;
        Test2 test2;
        List<Test2> test2List;
        Map<String, Test2> test2Map;
    }

    @Data
    static class Test2 {

        private int id;
        private int[] ids;
        private char charT;
        private char[] chars;
        private String str;
        private String[] strs;
        private List<String> stringList;
        private Date date;
    }

    @BeforeAll
    static void before() {
        Test1 test1 = new Test1();
        Test2 test2 = new Test2();
        test2.id = 1;
        test2.str = "test2";
        test2.stringList = Lists.newArrayList("t1", "t2");
        test1.test2 = test2;
        test1.test2List = Lists.newArrayList(test2);
        test1.test2Map = Maps.newHashMap();
        test1.test2Map.put("test2", test2);
        jsonObject = JSON.parseObject(test1);
    }

    @Test
    void containsKey() {
        assertTrue(jsonObject.containsKey("id"));
        assertTrue(jsonObject.containsKey("test2"));
        assertFalse(jsonObject.containsKey("test22"));
    }

    @Test
    void containsValue() {
    }

    @Test
    void get() {
    }

    @Test
    void getJSONObject() {
        System.out.println(jsonObject.getString("test2"));
    }

    @Test
    void getJSONArray() {
    }

    @Test
    void getObject() {
    }

    @Test
    void getBoolean() {
    }

    @Test
    void getBytes() {
    }

    @Test
    void getBooleanValue() {
    }

    @Test
    void getByte() {
    }

    @Test
    void getByteValue() {
    }

    @Test
    void getShort() {
    }

    @Test
    void getShortValue() {
    }

    @Test
    void getInteger() {
    }

    @Test
    void getIntValue() {
    }

    @Test
    void getLong() {
    }

    @Test
    void getLongValue() {
    }

    @Test
    void getFloat() {
    }

    @Test
    void getFloatValue() {
    }

    @Test
    void getDouble() {
    }

    @Test
    void getDoubleValue() {
    }

    @Test
    void getBigDecimal() {
    }

    @Test
    void getBigInteger() {
    }

    @Test
    void getString() {
    }

    @Test
    void getDate() {
    }
}
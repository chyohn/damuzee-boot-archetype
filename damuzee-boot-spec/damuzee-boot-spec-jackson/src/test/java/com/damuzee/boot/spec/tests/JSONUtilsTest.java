package com.damuzee.boot.spec.tests;

import com.damuzee.boot.spec.jackson.utils.JSONUtil;
import com.damuzee.boot.spec.jackson.utils.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 */
public class JSONUtilsTest {

    @Test
    public void testParseObject_typeReference() {
        Hello hello = new Hello();
        hello.setHelloContent("hello leo");
        System.out.println(JSON.toJSONString(hello));

        // for Object
        String helloStr = JSONUtil.toJSONString(hello);
        Hello helloDeserialized = JSONUtil.parseObject(helloStr, new TypeReference<Hello>() {
        });
        Assertions.assertEquals(hello.getHelloContent(), helloDeserialized.getHelloContent());

        // for map
        Hello hello2 = new Hello();
        hello2.setHelloContent("hello kai");
        Map<String, Hello> str2HelloMap = Maps.newHashMap();
        str2HelloMap.put("111", hello);
        str2HelloMap.put("222", hello2);
        String mapStr = JSONUtil.toJSONString(str2HelloMap);
        Map<String, Hello> deserializedMap = JSONUtil.parseObject(mapStr,
            new TypeReference<Map<String, Hello>>() {
            });
        Assertions.assertEquals("hello leo", deserializedMap.get("111").getHelloContent());
        Assertions.assertEquals("hello kai", deserializedMap.get("222").getHelloContent());

        // for list
        List<Hello> helloList = Lists.newArrayList(hello, hello2);
        String listStr = JSONUtil.toJSONString(helloList);
        List<Hello> deserializedList = JSONUtil.parseObject(listStr, new TypeReference<List<Hello>>() {
        });
        Assertions.assertEquals(2, deserializedList.size());
        Assertions.assertEquals("hello leo", deserializedList.get(0).getHelloContent());
        Assertions.assertEquals("hello kai", deserializedList.get(1).getHelloContent());

        // for multimap
        Map<String, Map<String, Hello>> multiMap = Maps.newHashMap();
        multiMap.put("xyz", str2HelloMap);
        String multiMapStr = JSONUtil.toJSONString(multiMap);
        Map<String, Map<String, Hello>> deserializedMultiMap = JSONUtil.parseObject(multiMapStr,
            new TypeReference<Map<String, Map<String, Hello>>>() {
            });
        Map<String, Hello> map = deserializedMultiMap.get("xyz");
        Assertions.assertEquals("hello leo", map.get("111").getHelloContent());
        Assertions.assertEquals("hello kai", map.get("222").getHelloContent());
    }

    @Test
    public void testValid() {
        Hello hello = new Hello();
        hello.helloContent = "test";
        String objstr = JSONUtil.toJSONString(hello);
        String arrStr = JSONUtil.toJSONString(com.google.common.collect.Lists.newArrayList(hello));
        System.out.println(objstr);
        System.out.println(arrStr);
        Assertions.assertTrue(JSONUtil.isValid(objstr));
        Assertions.assertTrue(JSONUtil.isValid(arrStr));

        Assertions.assertTrue(JSONUtil.isValidObject(objstr));
        Assertions.assertFalse(JSONUtil.isValidObject(arrStr));

        Assertions.assertFalse(JSONUtil.isValidArray(objstr));
        Assertions.assertTrue(JSONUtil.isValidArray(arrStr));

        Assertions.assertFalse(JSONUtil.isValid("{"));
        Assertions.assertFalse(JSONUtil.isValid("}"));
        Assertions.assertFalse(JSONUtil.isValid("{1}"));
        Assertions.assertFalse(JSONUtil.isValid("["));
        Assertions.assertFalse(JSONUtil.isValid("]"));
        Assertions.assertTrue(JSONUtil.isValid("[]"));
        Assertions.assertTrue(JSONUtil.isValid("{}"));
        Assertions.assertTrue(JSONUtil.isValid("12"));
        Assertions.assertTrue(JSONUtil.isValid("true"));

        Assertions.assertFalse(JSONUtil.isValidObject(" "));
        Assertions.assertFalse(JSONUtil.isValidObject("{"));
        Assertions.assertFalse(JSONUtil.isValidObject(null));
        Assertions.assertFalse(JSONUtil.isValidObject("}"));
        Assertions.assertFalse(JSONUtil.isValidObject("{1}"));
        Assertions.assertTrue(JSONUtil.isValidObject("{}"));

        Assertions.assertFalse(JSONUtil.isValidArray(" "));
        Assertions.assertFalse(JSONUtil.isValidArray(null));
        Assertions.assertFalse(JSONUtil.isValidArray("["));
        Assertions.assertFalse(JSONUtil.isValidArray("]"));
        Assertions.assertTrue(JSONUtil.isValidArray("[]"));
        Assertions.assertTrue(JSONUtil.isValidArray("[1]"));

    }

    @Data
    static class JSONObj {

        private String wareName;
    }


    @Getter
    @Setter
    public static class Hello {

        private String helloContent;
        private Integer testNoneField;
    }
}

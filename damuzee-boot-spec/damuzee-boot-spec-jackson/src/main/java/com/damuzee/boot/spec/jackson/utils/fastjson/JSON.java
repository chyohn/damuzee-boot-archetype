package com.damuzee.boot.spec.jackson.utils.fastjson;

import com.damuzee.boot.spec.jackson.utils.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 类似于fastjson接口
 */
public class JSON extends JSONUtil {

    public static String DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static TimeZone defaultTimeZone = TimeZone.getDefault();
    public static Locale defaultLocale = Locale.getDefault();
    public static String DEFAULT_TYPE_KEY = "@type";

    public static JSONObject parseObject(String text) {
        try {
            if (isEmpty(text)) {
                return null;
            }
            Map<String, Object> map = staticObjectMapper.readValue(text, new TypeReference<Map<String, Object>>() {
            });
            return new JSONObject(map);
        } catch (Exception e) {
            throw new JSONException("can not cast to JSONObject.", e);
        }
    }

    public static JSONObject parseObject(Object obj) {
        try {
            if (obj == null) {
                return null;
            }
            String objStr = staticObjectMapper.writeValueAsString(obj);
//			Map<String, Object> map = staticObjectMapper.readValue(objStr, new TypeReference<Map<String, Object>>() {});
            return parseObject(objStr);
        } catch (Exception e) {
            throw new JSONException("can not cast to JSONObject.", e);
        }
    }

    public static JSONArray parseArray(String text) {
        try {
            if (isEmpty(text)) {
                return null;
            }
            TypeReference<List<Object>> listTypeReference = new TypeReference<List<Object>>() {
            };
            List<Object> objList = staticObjectMapper.readValue(text, listTypeReference);
            return new JSONArray(objList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONArray parseArray(Object obj) {
        try {
            if (obj == null) {
                return null;
            }
            String objStr = staticObjectMapper.writeValueAsString(obj);
            TypeReference<List<Object>> listTypeReference = new TypeReference<List<Object>>() {
            };
            List<Object> objList = staticObjectMapper.readValue(objStr, listTypeReference);
            return new JSONArray(objList);
        } catch (Exception e) {
            throw new JSONException("can not cast to JSONObject.", e);
        }
    }

    // ======================================
    @Override
    public String toString() {
        return toJSONString(this);
    }

    public String toJSONString() {
        return toJSONString(this);
    }

}

package com.damuzee.boot.spec.jackson.utils.fastjson;


import com.damuzee.boot.spec.util.ObjectUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class JSONObject extends JSON implements Map<String, Object>, Serializable {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private final Map<String, Object> map;

    public JSONObject() {
        map = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
    }

    public JSONObject(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        this.map = new HashMap<>(map);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Object get(Object key) {
        Object val = map.get(key);
        if (val == null && key instanceof Number) {
            val = map.get(key.toString());
        }
        return val;
    }

    public JSONObject getJSONObject(String key) {
        Object value = map.get(key);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        if (value instanceof String) {
            return parseObject((String) value);
        }
        return parseObject(value);
    }

    public JSONArray getJSONArray(String key) {
        Object value = map.get(key);
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        if (value instanceof String) {
            return parseArray((String) value);
        }
        return parseArray(value);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object obj = map.get(key);
        return TypeUtils.castToJavaBean(obj, clazz);
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        return TypeUtils.castToBoolean(value);
    }

    public byte[] getBytes(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        return TypeUtils.castToBytes(value);
    }

    public boolean getBooleanValue(String key) {
        Object value = get(key);
        Boolean booleanVal = TypeUtils.castToBoolean(value);
        if (booleanVal == null) {
            return false;
        }
        return booleanVal.booleanValue();
    }

    public Byte getByte(String key) {
        Object value = get(key);
        return TypeUtils.castToByte(value);
    }

    public byte getByteValue(String key) {
        Object value = get(key);
        Byte byteVal = TypeUtils.castToByte(value);
        if (byteVal == null) {
            return 0;
        }
        return byteVal.byteValue();
    }

    public Short getShort(String key) {
        Object value = get(key);
        return TypeUtils.castToShort(value);
    }

    public short getShortValue(String key) {
        Object value = get(key);
        Short shortVal = TypeUtils.castToShort(value);
        if (shortVal == null) {
            return 0;
        }
        return shortVal.shortValue();
    }

    public Integer getInteger(String key) {
        Object value = get(key);
        return TypeUtils.castToInt(value);
    }

    public int getIntValue(String key) {
        Object value = get(key);
        Integer intVal = TypeUtils.castToInt(value);
        if (intVal == null) {
            return 0;
        }
        return intVal.intValue();
    }

    public Long getLong(String key) {
        Object value = get(key);
        return TypeUtils.castToLong(value);
    }

    public long getLongValue(String key) {
        Object value = get(key);
        Long longVal = TypeUtils.castToLong(value);
        if (longVal == null) {
            return 0L;
        }
        return longVal.longValue();
    }

    public Float getFloat(String key) {
        Object value = get(key);
        return TypeUtils.castToFloat(value);
    }

    public float getFloatValue(String key) {
        Object value = get(key);
        Float floatValue = TypeUtils.castToFloat(value);
        if (floatValue == null) {
            return 0F;
        }
        return floatValue.floatValue();
    }

    public Double getDouble(String key) {
        Object value = get(key);
        return TypeUtils.castToDouble(value);
    }

    public double getDoubleValue(String key) {
        Object value = get(key);
        Double doubleValue = TypeUtils.castToDouble(value);
        if (doubleValue == null) {
            return 0D;
        }
        return doubleValue.doubleValue();
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = get(key);
        return TypeUtils.castToBigDecimal(value);
    }

    public BigInteger getBigInteger(String key) {
        Object value = get(key);
        return TypeUtils.castToBigInteger(value);
    }

    /**
     * 获取key对应的value的字符串形式。
     * <p>
     * 注意：不一定是json格式，因为这里执行的是{@link Object#toString()}方法
     * <p>
     * 如果要获取json格式，使用{@link #getJSONString(String)}
     *
     * @param key
     * @return value的字符串形式
     */
    public String getString(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    /**
     * 获取key对应的value的json字符串
     * <p>
     * 注意：有性能开销，因为对于java bean、map、collection等对象，是通过执行{@link JSON#toJSONString()}方法获取其json串
     *
     * @param key
     * @return value的json字符串
     */
    public String getJSONString(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }

        // 简单类对象
        if (ObjectUtils.isSimpleObject(value)) {
            return value.toString();
        }

        // java bean、map、collection
        return toJSONString(value);
    }


    public Date getDate(String key) {
        Object value = get(key);
        return TypeUtils.castToDate(value);
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public JSONObject fluentPut(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        map.putAll(m);
    }

    public JSONObject fluentPutAll(Map<? extends String, ? extends Object> m) {
        map.putAll(m);
        return this;
    }

    public void clear() {
        map.clear();
    }

    public JSONObject fluentClear() {
        map.clear();
        return this;
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public JSONObject fluentRemove(Object key) {
        map.remove(key);
        return this;
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Collection<Object> values() {
        return map.values();
    }

    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
}

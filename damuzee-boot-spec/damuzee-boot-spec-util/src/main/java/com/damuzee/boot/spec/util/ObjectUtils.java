package com.damuzee.boot.spec.util;


import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;

/**
 */
public class ObjectUtils {


    /**
     * Check if the given type represents a "simple" value type obj
     *
     * @param obj
     * @return true is a simple type obj
     */
    public static boolean isSimpleObject(Object obj) {
        return ClassUtils.isSimpleType(obj.getClass());
    }

    public static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return null == iterable || isEmpty(iterable.iterator());
    }

    public static boolean isEmpty(Iterator<?> Iterator) {
        return null == Iterator || !Iterator.hasNext();
    }

    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            return isEmpty((CharSequence)obj);
        } else if (obj instanceof Map) {
            return isEmpty((Map)obj);
        } else if (obj instanceof Iterable) {
            return isEmpty((Iterable)obj);
        } else if (obj instanceof Iterator) {
            return isEmpty((Iterator)obj);
        } else {
            return isArray(obj) && 0 == Array.getLength(obj);
        }
    }

}

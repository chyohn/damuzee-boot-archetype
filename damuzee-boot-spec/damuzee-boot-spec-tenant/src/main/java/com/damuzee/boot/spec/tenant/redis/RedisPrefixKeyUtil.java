package com.damuzee.boot.spec.tenant.redis;


public final class RedisPrefixKeyUtil {

    /**
     * 普通key 生成方式 prefix:tenantId:originKey
     */
    public static String generate(String tenantId, String prefix, String originKey) {
        return prefix + ":" + tenantId + ":" + originKey;
    }


    /**
     * 使用twemproxy的key生成方式 prefix:{tenantId}:originKey
     */
    public static String generate4Twemproxy(String tenantId, String prefix, String originKey) {
        return prefix + ":" + "{" + tenantId + "}" + ":" + originKey;

    }


}

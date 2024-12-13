package com.damuzee.boot.spec.tenant;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 
 */
public class TenantEnvironment {

    private static final String REGION_TYPE = "SYM_DEPLOY_REGION_TYPE";
    private static final String GROUP_REGION_TYPE = "damuzee-group-id";

    /**
     * @return true 集团环境
     */
    public static boolean isGroup() {
        return GROUP_REGION_TYPE.equals(getSystemProperty(REGION_TYPE));
    }

    /**
     * 获取系统变量值
     *
     * @param key 变量key
     * @return 变量值
     */
    public static String getSystemProperty(String key) {
        String value = System.getenv(key);
        if (StringUtils.isEmpty(value)) {
            value = System.getProperty(key);
        }
        return value;
    }
}

package com.damuzee.boot.spec.env;

import com.damuzee.boot.spec.env.property.EnvironmentProperties;

/**
 * 环境变量工具类
 */
public class EnvUtils {

    private static EnvironmentProperties ENV_PROS = new EnvironmentProperties();

    public static void setEnvPros(EnvironmentProperties envPros) {
        ENV_PROS = envPros;
    }

    /**
     * @return 获取项目编码，比如：ware
     */
    public static String getProjectCode() {
        return ENV_PROS.getProjectCode();
    }

    /**
     * @return 获取应用编码，比如：ware-index
     */
    public static String getAppCode() {
        return ENV_PROS.getAppCode();
    }


    /**
     * @return 获取系统编码，比如：search
     */
    public static String getSystemCode() {
        return ENV_PROS.getSystemCode();
    }

    /**
     * @return true: 错误展示编码需要包含app code
     */
    public static boolean isDisplayCodeWithAppCode() {
        return ENV_PROS.isDisplayCodeWithAppCode();
    }

}

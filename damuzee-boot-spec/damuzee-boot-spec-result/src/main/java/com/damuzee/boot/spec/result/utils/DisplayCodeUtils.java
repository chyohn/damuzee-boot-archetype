package com.damuzee.boot.spec.result.utils;

import com.damuzee.boot.spec.env.EnvUtils;
import com.damuzee.boot.spec.result.enums.IResultCode;

/**
 * display code
 */
public class DisplayCodeUtils {

    /**
     * 只指定结果编码，产品域编码和系统编码通过环境变量获取
     *
     * @param resultCode 结果编码
     * @return display code
     */
    public static String buildCode(IResultCode resultCode) {
        return buildCode(resultCode.getCode());
    }

    /**
     * 只指定结果编码，产品域编码和系统编码通过环境变量获取
     *
     * @param resultCode 结果编码
     * @return display code
     */
    public static String buildCode(String resultCode) {
        String sysCode = EnvUtils.getSystemCode();
        return buildCode(sysCode, resultCode);
    }


    /**
     * 构造display code
     *
     * @param sysCode     系统编码
     * @param resultCode  结果编码
     * @return display code，字母都大写
     */
    public static String buildCode(String sysCode, String resultCode) {

        if (EnvUtils.isDisplayCodeWithAppCode()) {
            String appCode = EnvUtils.getAppCode();
            return String.join("-", sysCode, appCode, resultCode).toUpperCase();
        }

        return String.join("-", sysCode, resultCode).toUpperCase();
    }


}

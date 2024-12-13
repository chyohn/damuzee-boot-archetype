package com.damuzee.boot.spec.env.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 环境变量属性
 */
@Data
public class EnvironmentProperties {

    @Value("${damuzee.system.code}")
    private String systemCode;
    @Value("${damuzee.project.code}")
    private String projectCode;
    @Value("${damuzee.app.code}")
    private String appCode;

    /**
     * true: 错误展示编码包含app code
     */
    @Value("${damuzee.result.display-code-with-app-code:false}")
    private boolean displayCodeWithAppCode;

}

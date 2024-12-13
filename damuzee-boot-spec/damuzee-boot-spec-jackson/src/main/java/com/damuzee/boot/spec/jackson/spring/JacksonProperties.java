package com.damuzee.boot.spec.jackson.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "damuzee.jackson")
public class JacksonProperties {

    /**
     * 针对Local系列
     */
    private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private String dateFormat = "yyyy-MM-dd";
    private String timeFormat = "HH:mm:ss";
    /**
     * 针对java.util.Date
     */
    private String utilDateFormat;

    private JsonInclude.Include defaultPropertyInclusion;

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getUtilDateFormat() {
        return utilDateFormat;
    }

    public void setUtilDateFormat(String utilDateFormat) {
        this.utilDateFormat = utilDateFormat;
    }

    public JsonInclude.Include getDefaultPropertyInclusion() {
        return this.defaultPropertyInclusion;
    }

    public void setDefaultPropertyInclusion(JsonInclude.Include defaultPropertyInclusion) {
        this.defaultPropertyInclusion = defaultPropertyInclusion;
    }

}

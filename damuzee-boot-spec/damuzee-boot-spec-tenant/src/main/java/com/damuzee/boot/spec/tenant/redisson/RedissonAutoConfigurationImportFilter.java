package com.damuzee.boot.spec.tenant.redisson;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

import java.util.List;

public class RedissonAutoConfigurationImportFilter implements AutoConfigurationImportFilter {

    private static final List<String> excludes = Lists.newArrayList(
        "org.redisson.spring.starter.RedissonAutoConfiguration");

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        if (ArrayUtils.isEmpty(autoConfigurationClasses)) {
            return new boolean[0];
        }
        boolean[] result = new boolean[autoConfigurationClasses.length];
        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            String cls = autoConfigurationClasses[i];
            result[i] = !excludes.contains(cls);
        }
        return result;
    }

}

package com.damuzee.boot.spec.tenant.jdbc.sql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantConfigurationException extends RuntimeException {

    private String message;

    public TenantConfigurationException(final String message) {
        this.message = message;
    }

    public TenantConfigurationException(final String message, final String message1) {
        super(message);
        this.message = message1;
    }
}

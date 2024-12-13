package com.damuzee.boot.spec.tenant.lock;

public class DistributedLockException extends RuntimeException {

    protected String bizCode;

    public DistributedLockException(String bizCode, String message) {
        this(message);
        this.bizCode = bizCode;
    }

    public DistributedLockException(String message) {
        super(message);
    }

    public String getBizCode() {
        return bizCode;
    }
}

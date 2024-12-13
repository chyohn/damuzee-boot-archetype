package com.damuzee.boot.spec.tenant.lock;

public class DistributedLockIOException extends DistributedLockException {

    public DistributedLockIOException(String bizCode, String message) {
        super(bizCode, message);
    }
}

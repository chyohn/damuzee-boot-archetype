package com.damuzee.boot.spec.tenant.lock;

public class DistributedLockAcquireException extends DistributedLockException {

    public DistributedLockAcquireException(String bizCode, String message) {
        super(bizCode, message);
    }

}

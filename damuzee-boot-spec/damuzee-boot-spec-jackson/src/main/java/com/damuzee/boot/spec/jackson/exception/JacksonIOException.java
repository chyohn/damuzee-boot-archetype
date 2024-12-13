package com.damuzee.boot.spec.jackson.exception;

public class JacksonIOException extends RuntimeException {

    public JacksonIOException(String message) {
        super(message);
    }

    public JacksonIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public JacksonIOException(Throwable cause) {
        super(cause);
    }
}

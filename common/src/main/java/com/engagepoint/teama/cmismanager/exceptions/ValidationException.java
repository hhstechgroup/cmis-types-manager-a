package com.engagepoint.teama.cmismanager.exceptions;

public class ValidationException extends BaseException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

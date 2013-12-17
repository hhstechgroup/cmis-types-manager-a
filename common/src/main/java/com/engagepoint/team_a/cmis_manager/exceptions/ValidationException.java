package com.engagepoint.team_a.cmis_manager.exceptions;

public class ValidationException extends BaseException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

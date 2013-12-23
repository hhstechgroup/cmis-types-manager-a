package com.engagepoint.teama.cmismanager.common.exceptions;

public class ConvertationException extends ModificationException {

    public ConvertationException(String message) {
        super(message);
    }

    public ConvertationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

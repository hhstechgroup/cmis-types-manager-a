package com.engagepoint.teama.cmismanager.exceptions;


public class ConnectionException extends BaseException {
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

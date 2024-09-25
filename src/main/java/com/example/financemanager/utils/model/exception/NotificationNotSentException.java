package com.example.financemanager.utils.model.exception;

public class NotificationNotSentException extends RuntimeException {
    public NotificationNotSentException(String message, Throwable cause) {
        super(message, cause);
    }
}

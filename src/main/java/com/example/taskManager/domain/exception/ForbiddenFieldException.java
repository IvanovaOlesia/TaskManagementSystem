package com.example.taskManager.domain.exception;


public class ForbiddenFieldException extends RuntimeException {
    public ForbiddenFieldException(String message) {
        super(message);
    }
}

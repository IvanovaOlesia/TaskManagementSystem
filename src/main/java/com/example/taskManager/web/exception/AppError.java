package com.example.taskManager.web.exception;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AppError {
    private int status;
    private String message;
    private Date timestamp;
    private List<String> errors;


    public AppError(int status, String message, Date timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
    public AppError(List<String> errors) {
        this.errors = errors;
    }
}

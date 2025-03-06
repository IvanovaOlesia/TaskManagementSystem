package com.example.taskManager.web.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(String.format("Invalid input: %s", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(String.format("Validation error: %s", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<String> handleMismatchedInputException(MismatchedInputException ex) {
        return new ResponseEntity<>(String.format("Invalid input: %s", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        String errorMessage = String.format("%s method is not supported. Available methods: %s", ex.getMethod(), supportedMethods);
        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Invalid argument: %s. Expected type: %s",
                ex.getValue(), ex.getRequiredType().getSimpleName());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppError> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errorMessages = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.toList());

        AppError response = new AppError(errorMessages);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

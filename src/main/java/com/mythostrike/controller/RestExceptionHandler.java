package com.mythostrike.controller;

import com.mythostrike.model.exception.IllegalInputException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public void handleResponseStatusException(ResponseStatusException exception) {
        List<StackTraceElement> limitedStackTrace = Arrays.stream(exception.getStackTrace()).limit(5).toList();
        log.error("ResponseStatusException: '{}' at '{}'", exception.getReason(), limitedStackTrace);
        throw exception;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception,
                                                                 WebRequest request) {
        List<StackTraceElement> limitedStackTrace = Arrays.stream(exception.getStackTrace()).limit(5).toList();
        log.error("IllegalArgumentException: '{}' at '{}'", exception.getMessage(), limitedStackTrace);


        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
            HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleIEntityNotFoundException(EntityNotFoundException exception,
                                                                    WebRequest request) {
        List<StackTraceElement> limitedStackTrace = Arrays.stream(exception.getStackTrace()).limit(5).toList();
        log.error("EntityNotFoundException: '{}' at '{}'", exception.getMessage(), limitedStackTrace);

        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
            HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(IllegalInputException.class)
    public ResponseEntity<Object> handleIIllegalInputException(IllegalInputException exception,
                                                               WebRequest request) {
        List<StackTraceElement> limitedStackTrace = Arrays.stream(exception.getStackTrace()).limit(5).toList();
        log.error("IllegalInputException: '{}' at '{}'", exception.getMessage(), limitedStackTrace);

        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
            HttpStatus.BAD_REQUEST, request);
    }
}

package com.mythostrike.controller;

import com.mythostrike.model.exception.IllegalInputException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseStatusException handleResponseStatusException(ResponseStatusException exception) {
        log.debug("ResponseStatusException: '{}'", exception.getMessage());
        throw new ResponseStatusException(exception.getStatusCode(), exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseStatusException handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("IllegalArgumentException: '{}'", exception.getMessage());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseStatusException handleIEntityNotFoundException(EntityNotFoundException exception) {
        log.error("EntityNotFoundException: '{}'", exception.getMessage());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IllegalInputException.class)
    public ResponseStatusException handleIIllegalInputException(IllegalInputException exception) {
        log.error("IllegalInputException: '{}'", exception.getMessage());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}

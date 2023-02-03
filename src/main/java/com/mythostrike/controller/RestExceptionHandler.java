package com.mythostrike.controller;

import com.mythostrike.model.exception.IllegalInputException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException exception) {
        return new ResponseEntity<>(exception.getReason(), exception.getStatusCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("Illegal Argument Exception: ", exception);
        return new ResponseEntity<>("ERROR: " + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleIEntityNotFoundException(EntityNotFoundException exception) {
        log.error("EntityNotFoundException: ", exception);
        return new ResponseEntity<>("ERROR: " + exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalInputException.class)
    public ResponseEntity<String> handleIIllegalInputException(IllegalInputException exception) {
        log.error("IllegalInputException: ", exception);
        return new ResponseEntity<>("ERROR: " + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

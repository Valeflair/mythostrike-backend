package com.mythostrike.model.exception;

public class IllegalInputException extends RuntimeException {
    public IllegalInputException() {
        super("Data not correct!");
    }

    public IllegalInputException(String message) {
        super(message);
    }
}

package com.mythostrike.model.exception;

public class IllegalInputException extends Exception {
    public IllegalInputException() {
        super("Data not correct!");
    }

    public IllegalInputException(String message) {
        super(message);
    }
}

package com.mythostrike.controller.message;

import org.springframework.http.HttpStatus;

public record ErrorMessage(String error, String message, String path, int status, long timestamp) {

    public ErrorMessage(HttpStatus status, String path, String message) {
        this(status.getReasonPhrase(), message, path, status.value(), System.currentTimeMillis());
    }

}

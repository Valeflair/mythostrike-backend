package com.mythostrike.controller.message.game;

public record LogMessage(WebsocketGameMessageType messageType, String message) {

    public LogMessage(String message) {
        this(WebsocketGameMessageType.LOG, message);
    }
}

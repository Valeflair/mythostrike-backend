package com.mythostrike.controller.message.game;

public record WebSocketGameMessage(WebSocketGameMessageType messageType, Object payload) {
}

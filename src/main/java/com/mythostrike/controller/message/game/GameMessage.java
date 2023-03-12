package com.mythostrike.controller.message.game;

public record GameMessage(GameMessageType messageType, Object payload) {
}

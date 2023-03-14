package com.mythostrike.controller.message.game;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = GameMessageDeserializer.class)
public record GameMessage(GameMessageType messageType, Object payload) {
}

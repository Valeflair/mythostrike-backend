package com.mythostrike.controller.message.game;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

class GameMessageDeserializer extends JsonDeserializer<GameMessage> {
    @Override
    public GameMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.readValueAsTree();

        GameMessageType type = GameMessageType.valueOf(node.get("messageType").asText());
        ObjectMapper objectMapper = new ObjectMapper();
        GameMessage message;

        switch (type) {
            case HIGHLIGHT -> {
                message = new GameMessage(type, objectMapper.treeToValue(node.get("payload"), HighlightMessage.class));
            }
            case UPDATE_GAME -> {
                PlayerData[] payload = objectMapper.treeToValue(node.get("payload"), PlayerData[].class);
                message = new GameMessage(type, List.of(payload));
            }
            case CARD_MOVE -> {
                message = new GameMessage(type, objectMapper.treeToValue(node.get("payload"), CardMoveMessage.class));
            }
            case LOG -> {
                message = new GameMessage(type, objectMapper.treeToValue(node.get("payload"), LogMessage.class));
            }
            case GAME_END -> {
                message = new GameMessage(type, objectMapper.treeToValue(node.get("payload"), PlayerGameResult.class));
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return message;
    }
}

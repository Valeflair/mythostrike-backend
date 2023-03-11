package com.mythostrike.controller.message.lobby;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.mythostrike.model.lobby.Identity;

import java.io.IOException;

class SeatMessageDeserializer extends JsonDeserializer<SeatMessage> {
    @Override
    public SeatMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.readValueAsTree();

        // If the username is null, then the seat is empty. Only the identity is present.
        if (node.findValue("username") == null) {
            String identity = node.get("identity").asText();
            return new SeatMessage(Identity.getIdentityWithName(identity));
        }
        String username = node.get("username").asText();
        String identity = node.get("identity").asText();
        int avatarNumber = node.get("avatarNumber").asInt();
        return new SeatMessage(username, avatarNumber, Identity.getIdentityWithName(identity));
    }
}

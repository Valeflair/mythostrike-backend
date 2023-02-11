package com.mythostrike.controller.message.game;

import com.mythostrike.model.lobby.Identity;

public record GameEndMessage(WebsocketGameMessageType messageType, String username, Identity identity, int drachma,
                             int rankPoints, boolean hasWon) {

    public GameEndMessage(String username, Identity identity, int drachma, int rankPoints, boolean hasWon) {
        this(WebsocketGameMessageType.GAME_END, username, identity, drachma, rankPoints, hasWon);
    }
}

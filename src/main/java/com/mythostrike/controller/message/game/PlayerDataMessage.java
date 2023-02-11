package com.mythostrike.controller.message.game;

import com.mythostrike.model.game.player.Champion;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.Identity;

public record PlayerDataMessage(WebsocketGameMessageType messageType, String username, int cardCount, boolean isAlive,
                                Champion champion, int maxHp, int currentHp, Identity identity) {
    public PlayerDataMessage(Player player) {
        this(WebsocketGameMessageType.UPDATE_GAME, player.getUsername(), player.getHandCards().size(), player.isAlive(),
            player.getChampion(), player.getMaxHp(), player.getCurrentHp(), player.getIdentity());
    }
}

package com.mythostrike.controller.message.game;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mythostrike.controller.message.lobby.ChampionMessage;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.Identity;

import java.util.List;

public record PlayerData(String username, int cardCount, boolean isAlive,
                         ChampionMessage champion, int maxHp, int currentHp, Identity identity, boolean isCurrentPlayer,
                         List<Integer> equipment, List<Integer> delayedEffects) {
    public PlayerData(Player player, boolean isCurrentPlayer) {
        this(player.getUsername(), player.getHandCards().size(), player.isAlive(),
            new ChampionMessage(player.getChampion()), player.getMaxHp(), player.getCurrentHp(), player.getIdentity(),
            isCurrentPlayer, player.getEquipment().getCards().stream().map(Activity::getId).toList(),
            player.getDelayedEffect().getCards().stream().map(Activity::getId).toList());
    }

    @JsonGetter("identity")
    private String getIdentityString() {
        if (identity.isIncognito()) {
            return Identity.NONE.toString();
        } else {
            return identity.toString();
        }
    }
}

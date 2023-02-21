package com.mythostrike.model.lobby;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.mythostrike.controller.message.lobby.SeatMessage;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Seat {
    @JsonIgnore
    private final int id;
    @Setter
    private Player player;
    @Setter
    private Identity identity;

    public Seat(int id, Player player, Identity identity) {
        this.id = id;
        this.player = player;
        this.identity = identity;
    }

    @JsonGetter("player")
    @JsonUnwrapped
    private SeatMessage modeToString() {
        if (player == null) {
            return null;
        }
        return new SeatMessage(player);
    }

    @JsonGetter("identity")
    private String identityToString() {
        if (identity.isIncognito()) {
            return Identity.NONE.getName();
        } else {
            return identity.getName();
        }
    }
}

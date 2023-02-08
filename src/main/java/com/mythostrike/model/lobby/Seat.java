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

    public Seat(int id, Player player) {
        this.id = id;
        this.player = player;
    }

    public Seat(int id) {
        this(id, null);
    }

    @JsonGetter("player")
    @JsonUnwrapped
    private SeatMessage modeToString() {
        if (player == null) {
            return null;
        }
        return new SeatMessage(player);
    }
}

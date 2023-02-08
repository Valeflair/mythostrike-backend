package com.mythostrike.model.lobby;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mythostrike.model.game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Seat {
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
    private String modeToString() {
        if (player == null) {
            return "";
        }
        return player.getUsername();
    }
}

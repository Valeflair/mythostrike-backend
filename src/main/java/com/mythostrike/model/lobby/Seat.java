package com.mythostrike.model.lobby;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mythostrike.model.game.Player;
import lombok.Getter;
import lombok.Setter;

public class Seat {
    @JsonValue
    private final int id;

    @Getter
    @Setter
    private Player player;

    public Seat(int id, Player player) {
        this.id = id;
        this.player = player;
    }

    public Seat(int id) {
        this(id, null);
    }
}

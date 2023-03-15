package com.mythostrike.model.lobby;

import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class Seat {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat seat = (Seat) o;

        return id == seat.id && Objects.equals(player, seat.player) && identity == seat.identity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, player, identity);
    }
}

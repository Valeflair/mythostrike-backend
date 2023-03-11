package com.mythostrike.controller.message.lobby;

import com.mythostrike.model.lobby.Lobby;

import java.util.List;
import java.util.Objects;

public record LobbyMessage(int id, String mode, String owner, List<SeatMessage> seats) {

    public LobbyMessage(Lobby lobby) {
        this(lobby.getId(), lobby.getMode().name(), lobby.getOwner().getUsername(),
            lobby.getSeats().stream().map(SeatMessage::new).toList()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LobbyMessage other = (LobbyMessage) o;

        return id == other.id && mode.equals(other.mode) && owner.equals(other.owner) && seats.equals(other.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }
}

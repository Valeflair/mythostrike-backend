package com.mythostrike.controller.message.lobby;

import java.util.Objects;

public record SeatPlayerMessage(String username, int avatarNumber) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeatPlayerMessage other = (SeatPlayerMessage) o;

        return avatarNumber == other.avatarNumber && username.equals(other.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, avatarNumber);
    }
}

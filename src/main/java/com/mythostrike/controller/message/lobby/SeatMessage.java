package com.mythostrike.controller.message.lobby;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mythostrike.model.lobby.Identity;
import com.mythostrike.model.lobby.Seat;
import lombok.Getter;

import java.util.Objects;

@Getter
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(using  = SeatMessageDeserializer.class)
public class SeatMessage {
    @JsonUnwrapped
    private final SeatPlayerMessage player;
    private final Identity identity;

    /**
     * This constructor is used for deserialization.
     */
    public SeatMessage() {
        this.player = null;
        this.identity = null;
    }

    public SeatMessage(Seat seat) {
        if (seat.getPlayer() == null) {
            player = null;
        } else {
            player = new SeatPlayerMessage(seat.getPlayer().getUsername(), seat.getPlayer().getAvatarNumber());
        }
        this.identity = seat.getIdentity();
    }

    public SeatMessage(String username, int avatarNumber, Identity identity) {
        player = new SeatPlayerMessage(username, avatarNumber);
        this.identity = identity;
    }

    public SeatMessage(Identity identity) {
        this.player = null;
        this.identity = identity;
    }

    @JsonGetter("identity")
    private String identityToString() {
        if (identity.isIncognito()) {
            return Identity.NONE.getName();
        } else {
            return identity.getName();
        }
    }

    @Override
    public String toString() {
        if (player == null) {
            return String.format("{identity=%s}", identity);
        }
        return String.format("{username=%s, avatarNumber=%d, identity=%s}",
            player.username(), player.avatarNumber(), identity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeatMessage other = (SeatMessage) o;

        return Objects.equals(player, other.player) && identity == other.identity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, identity);
    }
}

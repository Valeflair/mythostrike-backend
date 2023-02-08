package com.mythostrike.controller.message.lobby;

import com.mythostrike.model.game.player.Player;

public record SeatMessage(String username, int avatarNumber) {

    public SeatMessage(Player player) {
        this(player.getUsername(), player.getAvatarNumber());
    }
}

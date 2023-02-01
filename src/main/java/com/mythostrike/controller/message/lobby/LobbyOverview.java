package com.mythostrike.controller.message.lobby;

import com.mythostrike.model.lobby.Lobby;

public record LobbyOverview(int id, String owner, String status, String mode, int numberPlayers) {

    public LobbyOverview(Lobby lobby) {
        this(lobby.getId(), lobby.getOwner().getUsername(), lobby.getStatus().toString(), lobby.getMode().name(),
            lobby.getNumberPlayers());
    }

}

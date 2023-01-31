package com.mythostrike.model.lobby;

import com.mythostrike.controller.message.lobby.LobbyOverview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LobbyList {
    private static LobbyList instance;
    private final Map<Integer, Lobby> lobbyMap;

    /**
     * This is a Singelton Class. The Constructor is private.
     * Use the getLobbyList() method to get the instance.
     */
    private LobbyList() {
        this.lobbyMap = new HashMap<>();

    }

    public static LobbyList getLobbyList() {
        if (instance == null) {
            instance = new LobbyList();
        }
        return instance;
    }

    public List<LobbyOverview> getLobbiesOverview() {
        List<LobbyOverview> lobbiesOverview = new ArrayList<>();
        for (Lobby lobby : lobbyMap.values()) {
            lobbiesOverview.add(new LobbyOverview(lobby));
        }
        return lobbiesOverview;
    }

    public Lobby getLobby(int id) {
        return lobbyMap.get(id);
    }
}

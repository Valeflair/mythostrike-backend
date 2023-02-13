package com.mythostrike.model.lobby;

import com.mythostrike.account.repository.User;
import com.mythostrike.controller.message.lobby.LobbyOverview;
import com.mythostrike.model.game.management.GameManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LobbyList {
    private static LobbyList instance;
    private final Map<Integer, Lobby> lobbyMap;

    private final Map<Integer, Integer> userInGame;
    private int idCounter;

    /**
     * This is a Singelton Class. The Constructor is private.
     * Use the getLobbyList() method to get the instance.
     */
    private LobbyList() {
        idCounter = 1;
        this.lobbyMap = new HashMap<>();
        this.userInGame = new HashMap<>();

    }

    public static LobbyList getLobbyList() {
        if (instance == null) {
            instance = new LobbyList();
        }
        return instance;
    }

    public Lobby createLobby(User owner, Mode mode) {
        Lobby lobby = new Lobby(idCounter, mode, owner);
        lobbyMap.put(idCounter, lobby);
        userInGame.put(idCounter, 0);
        idCounter++;
        return lobby;
    }

    public void removeLobby(int id) {
        lobbyMap.remove(id);
    }

    public List<LobbyOverview> getLobbiesOverview() {
        List<LobbyOverview> lobbiesOverview = new ArrayList<>();
        for (Lobby lobby : lobbyMap.values()) {
            lobbiesOverview.add(new LobbyOverview(lobby));
        }
        return lobbiesOverview;
    }

    public void increaseUserInGame(int id) {
        userInGame.put(id, userInGame.get(id) + 1);
    }

    public boolean isInGameFull(int id) {
        return userInGame.get(id) >= lobbyMap.get(id).getNumberHumans();
    }

    @Nullable
    public Lobby getLobby(int id) {
        return lobbyMap.get(id);
    }

    @Nullable
    public GameManager getGameManager(int id) {
        Lobby lobby = lobbyMap.get(id);
        if (lobby == null) return null;
        return lobby.getGameManager();
    }
}

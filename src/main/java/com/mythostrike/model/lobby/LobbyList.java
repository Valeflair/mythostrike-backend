package com.mythostrike.model.lobby;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.lobby.LobbyOverview;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.management.GameManager;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mythostrike.controller.LobbyController.LOBBY_NOT_FOUND_MESSAGE;

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

    public Lobby createLobby(User owner, Mode mode, UserService userService) {
        Lobby lobby = new Lobby(idCounter, mode, owner, userService);
        lobbyMap.put(idCounter, lobby);
        userInGame.put(idCounter, 0);
        idCounter++;
        return lobby;
    }

    /**
     * removes the user from the lobby and removes the lobby if it is empty
     * @param lobbyId id of the lobby to remove the user from
     * @param user user to remove
     * @return true if the lobby was removed
     */
    public boolean removeUser(int lobbyId, User user) {
        Lobby lobby = getLobby(lobbyId);
        if (lobby == null) {
            throw new IllegalInputException(LOBBY_NOT_FOUND_MESSAGE);
        }

        //leave user from lobby
        if (!lobby.removeUser(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not in lobby");
        }

        //remove lobby if empty
        if (lobby.canBeDeleted()) {
            removeLobby(lobbyId);
            return true;
        }
        return false;
    }

    public boolean removeUser(int lobbyId, String username) {
        Lobby lobby = getLobby(lobbyId);
        if (lobby == null) {
            throw new IllegalInputException(LOBBY_NOT_FOUND_MESSAGE);
        }

        //leave user from lobby
        if (!lobby.removeUser(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not in lobby");
        }

        //remove lobby if empty
        if (lobby.canBeDeleted()) {
            removeLobby(lobbyId);
            return true;
        }
        return false;
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

    public void decreaseUserInGame(int id) {
        userInGame.put(id, userInGame.get(id) - 1);
    }

    public boolean isGameReadyToStart(int id) {
        return (userInGame.get(id) >= lobbyMap.get(id).getNumberHumans())
            && (lobbyMap.get(id).getStatus() == LobbyStatus.FULL);
    }

    public void setLobbyGameRunning(int id) {
        Lobby lobby = lobbyMap.get(id);
        if (lobby == null) throw new IllegalInputException("Lobby not found");
        lobby.setStatus(LobbyStatus.GAME_RUNNING);
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

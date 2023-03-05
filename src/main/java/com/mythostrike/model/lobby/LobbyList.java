package com.mythostrike.model.lobby;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.lobby.LobbyOverview;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.management.GameManager;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mythostrike.controller.LobbyController.LOBBY_NOT_FOUND_MESSAGE;

@Slf4j
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
        userInGame.remove(id);
    }

    public List<LobbyOverview> getLobbiesOverview() {
        List<LobbyOverview> lobbiesOverview = new ArrayList<>();
        for (Lobby lobby : lobbyMap.values()) {
            lobbiesOverview.add(new LobbyOverview(lobby));
        }
        return lobbiesOverview;
    }

    /**
     * increases the number of users in game by one
     * @param id the id of the lobby
     */
    public void increaseUserInGame(int id) {
        Integer usersInGame = userInGame.get(id);
        if (usersInGame == null) return;
        userInGame.put(id, usersInGame + 1);
    }

    /**
     * decreases the number of users in game by one.
     * If the number of users in game is 0, then the game gets stopped and deleted.
     * @param id the id of the lobby
     */
    public void decreaseUserInGame(int id) {
        Integer usersInGame = userInGame.get(id);
        if (usersInGame == null) return;
        usersInGame--;

        if (usersInGame == 0) {
            log.debug("removeing game, because no user subscribed anymore");
            removeLobby(id);
        } else {
            userInGame.put(id, usersInGame);
        }
    }

    public boolean isGameReadyToStart(int id) {
        Lobby lobby = lobbyMap.get(id);
        if (lobby == null) throw new IllegalInputException("Lobby not found");
        return (userInGame.get(id) >= lobby.getNumberHumans())
            && (lobby.getStatus() == LobbyStatus.CHAMPION_SELECTION);
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

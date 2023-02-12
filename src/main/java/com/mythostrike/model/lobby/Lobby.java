package com.mythostrike.model.lobby;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mythostrike.account.repository.User;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Bot;
import com.mythostrike.model.game.player.Human;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class Lobby {
    private static final int MAX_PLAYERS = 8;
    private final int id;
    private final Seat[] seats;
    private Mode mode;
    private Player owner;
    @JsonIgnore
    private LobbyStatus status;
    @JsonIgnore
    private int numberPlayers;
    @JsonIgnore
    private int numberHumans;
    @JsonIgnore
    private GameManager gameManager;

    public Lobby(int id, Mode mode, User owner) {
        this.id = id;
        this.mode = mode;
        this.owner = new Human(owner);
        this.status = LobbyStatus.OPEN;

        this.numberPlayers = 1;
        this.numberHumans = 1;
        //initialize seats array
        this.seats = new Seat[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++) {
            this.seats[i] = new Seat(i);
        }
        seats[0].setPlayer(this.owner);
    }

    @JsonGetter("mode")
    private String modeToString() {
        return mode.name();
    }

    @JsonGetter("owner")
    private String ownerToString() {
        return owner.getUsername();
    }

    @JsonIgnore
    public boolean isFull() {
        return numberPlayers >= mode.maxPlayer();
    }

    public boolean addUser(User user) throws IllegalInputException {
        if (isFull()) {
            return false;
        }
        //check if user is already in the lobby
        for (Seat seat : seats) {
            if (seat.getPlayer() != null && seat.getPlayer().getUsername().equals(user.getUsername())) {
                throw new IllegalInputException("user is already in the lobby");
            }
        }
        //add user to the first free seat
        for (Seat seat : seats) {
            if (seat.getPlayer() == null) {
                seat.setPlayer(new Human(user));
                this.numberPlayers++;
                this.numberHumans++;
                break;
            }
        }
        updateLobbyStatus();
        return true;
    }

    public boolean removeUser(User user) {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            //check if in which seat the user is
            if (seats[i].getPlayer() != null && seats[i].getPlayer().getUsername().equals(user.getUsername())) {
                seats[i].setPlayer(null);
                this.numberPlayers--;
                this.numberHumans--;

                //select new owner if the owner left
                if (user.getUsername().equals(owner.getUsername())) {
                    selectNewOwner();
                }
                //if the lobby is empty, close it
                if (numberHumans == 0) {
                    status = LobbyStatus.CLOSED;
                }
                updateLobbyStatus();
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean canBeDeleted() {
        return numberHumans == 0;
    }

    private void selectNewOwner() {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (seats[i].getPlayer() instanceof Human) {
                owner = seats[i].getPlayer();
                return;
            }
        }
    }

    private void updateLobbyStatus() {
        if (numberPlayers >= mode.maxPlayer()) {
            status = LobbyStatus.FULL;
        } else {
            status = LobbyStatus.OPEN;
        }
    }

    public boolean addBot(User user) throws IllegalInputException {
        if (!user.getUsername().equals(owner.getUsername())) {
            throw new IllegalInputException("user is not the owner");
        }
        if (isFull()) {
            return false;
        }
        //TODO: echten Bot erstellen
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (seats[i].getPlayer() == null) {
                seats[i].setPlayer(new Bot("Bot" + i, 1));
                numberPlayers++;
                updateLobbyStatus();
                return true;
            }
        }
        return false;
    }

    public boolean changeSeat(int seatId, User user) throws IllegalInputException {
        //check if seatId is valid
        if (seatId < 0 || seatId >= MAX_PLAYERS) {
            throw new IllegalInputException("seatId is not in bounds");
        }
        //check if seat is already taken
        if (seats[seatId].getPlayer() != null) {
            return false;
        }

        //check if user is in the lobby and remove him
        if (!removeUser(user)) {
            throw new IllegalInputException("user is not in lobby");
        }

        seats[seatId].setPlayer(new Human(user));
        this.numberPlayers++;
        this.numberHumans++;
        return true;
    }

    public void changeMode(Mode mode, User user) throws IllegalInputException {
        if (isNotOwner(user)) {
            throw new IllegalInputException("user is not the owner");
        }

        if (gameManager != null) {
            throw new IllegalInputException("lobby is not in Lobby Screen");
        }
        updateLobbyStatus();
        this.mode = mode;
    }

    public boolean createGame(User user) throws IllegalInputException {
        if (isNotOwner(user)) {
            throw new IllegalInputException("user is not the owner");
        }

        if (numberPlayers < mode.minPlayer() || numberPlayers > mode.maxPlayer()) {
            return false;
        }
        status = LobbyStatus.GAME_RUNNING;

        List<Player> players = new ArrayList<>(
            Arrays.stream(seats).map(Seat::getPlayer).filter(Objects::nonNull).toList()
        );

        //randomize positions if mode is Identity (except God King)
        if (mode.isFrom(ModeData.IDENTITY_FOR_EIGHT) || mode.isFrom(ModeData.IDENTITY_FOR_FIVE)) {
            Player godKing = players.remove(0);
            Collections.shuffle(players, Game.RANDOM_SEED);
            players.add(0, godKing);
        }

        //set identities depending on position
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setIdentity(mode.identityList().get(i));
        }

        gameManager = new GameManager(players, mode, id);
        gameManager.gameStart();
        return true;
    }

    private boolean isNotOwner(User user) {
        return !user.getUsername().equals(owner.getUsername());
    }
}

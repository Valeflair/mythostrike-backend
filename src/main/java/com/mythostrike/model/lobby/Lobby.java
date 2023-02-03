package com.mythostrike.model.lobby;

import com.mythostrike.account.repository.User;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Lobby {
    private static final int MAX_PLAYERS = 8;

    private final int id;
    private Mode mode;
    private Player owner;
    private LobbyStatus status;
    private final Seat[] seats;
    private int numberPlayers;


    public Lobby(int id, Mode mode, User owner) {
        this.id = id;
        this.mode = mode;
        this.owner = new Player(owner);
        this.status = LobbyStatus.OPEN;

        this.numberPlayers = 1;
        //initialize seats array
        this.seats = new Seat[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++) {
            this.seats[i] = new Seat(i);
        }
        seats[0].setPlayer(new Player(owner));
    }

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
        for (Seat seat: seats) {
            if (seat.getPlayer() == null) {
                seat.setPlayer(new Player(user));
                numberPlayers++;
            }
        }
        updateModeStatus();
        return true;
    }

    public boolean removeUser(User user) {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            //check if in which seat the user is
            if (seats[i].getPlayer() != null && seats[i].getPlayer().getUsername().equals(user.getUsername())) {
                seats[i].setPlayer(null);

                //select new owner if the owner left
                if (user.getUsername().equals(owner.getUsername())) {
                    selectNewOwner();
                }
                numberPlayers--;

                //if the lobby is empty, close it
                if (numberPlayers == 0) {
                    status = LobbyStatus.CLOSED;
                }
                updateModeStatus();
                return true;
            }
        }
        return false;
    }

    private void selectNewOwner() {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (seats[i].getPlayer() != null) {
                owner = seats[i].getPlayer();
                return;
            }
        }
    }

    private void updateModeStatus() {
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
                seats[i].setPlayer(new Player("Bot" + i));
                numberPlayers++;
                updateModeStatus();
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

        seats[seatId].setPlayer(new Player(user));
        return true;
    }

    public void changeMode(Mode mode, User user) throws IllegalInputException {
        if (!user.getUsername().equals(owner.getUsername())) {
            throw new IllegalInputException("user is not the owner");
        }
        //TODO: mit game != null abfrage ersetzen
        if (status != LobbyStatus.OPEN && status != LobbyStatus.FULL) {
            throw new IllegalInputException("lobby is not in Lobby Screen");
        }
        updateModeStatus();
        this.mode = mode;
    }

    public boolean createGame(User user) throws IllegalInputException {
        if (!user.getUsername().equals(owner.getUsername())) {
            throw new IllegalInputException("user is not the owner");
        }

        //TODO: complete this method
        //TODO: randomize identities if Identity mode --> not the God King
        if (numberPlayers >= mode.minPlayer() && numberPlayers <= mode.maxPlayer()) {
            status = LobbyStatus.GAME_RUNNING;
            return true;
        }
        return false;
    }
}

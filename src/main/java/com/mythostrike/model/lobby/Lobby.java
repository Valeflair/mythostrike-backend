package com.mythostrike.model.lobby;

import com.mythostrike.account.repository.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Lobby {
    private static final int MAX_PLAYERS = 8;

    private final int id;
    @Setter
    private Mode mode;
    private Player owner;
    private LobbyStatus status;
    private Seat[] seats;
    private int numberOfPlayers;


    public Lobby(int id, Mode mode, User owner) {
        this.id = id;
        this.mode = mode;
        this.owner = new Player(owner);
        this.status = LobbyStatus.OPEN;

        this.numberOfPlayers = 1;
        //initialize seats array
        this.seats = new Seat[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++) {
            this.seats[i] = new Seat(i);
        }
        seats[0].setPlayer(new Player(owner));
    }

    public boolean isFull() {
        return numberOfPlayers == mode.maxPlayer();
    }

    public boolean changeSeat(int seatId, User user) {
        //check if seatId is valid
        if (seatId < 0 || seatId >= MAX_PLAYERS) {
            return false;
        }
        //check if seat is already taken
        if (seats[seatId].getPlayer() != null) {
            return false;
        }
        //check if user is in the lobby and remove him
        if (!removeUser(user)) {
            return false;
        }

        seats[seatId].setPlayer(new Player(user));
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
                numberOfPlayers--;

                //if the lobby is empty, close it
                if (numberOfPlayers == 0) {
                    status = LobbyStatus.CLOSED;
                }
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

    public boolean addUser(User user) {
        if (isFull()) {
            return false;
        }
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (seats[i].getPlayer() == null) {
                seats[i].setPlayer(new Player(user));
                numberOfPlayers++;
                return true;
            }
        }
        return false;
    }

    public boolean createGame() {
        //TODO: complete this method
        //TODO: randomize identites if Identity mode --> not the God King
        if (isFull()) {
            status = LobbyStatus.GAME_RUNNING;
            return true;
        }
        return false;
    }
}

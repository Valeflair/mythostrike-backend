package com.mythostrike.model.lobby;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.GameController;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Human;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.game.player.RandomBot;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class Lobby {
    private final int id;
    private final UserService userService;
    private List<Seat> seats;
    private Mode mode;
    private Player owner;
    @Setter
    private LobbyStatus status;
    private int numberPlayers;
    private int numberHumans;
    private GameManager gameManager;

    public Lobby(int id, Mode mode, User owner, UserService userService) {
        this.id = id;
        this.mode = mode;
        this.owner = new Human(owner, userService);
        this.status = LobbyStatus.OPEN;
        this.userService = userService;

        this.numberPlayers = 1;
        this.numberHumans = 1;
        //initialize seats array
        this.seats = new ArrayList<>(mode.maxPlayer());
        List<Identity> identities = mode.getIdentityList();
        for (int i = 0; i < mode.maxPlayer(); i++) {
            seats.add(new Seat(i, null, identities.get(i)));
        }
        seats.get(0).setPlayer(this.owner);
    }

    private boolean isFull() {
        return numberPlayers >= mode.maxPlayer();
    }

    public boolean addUser(User user) throws IllegalInputException {
        if (isFull()) {
            return false;
        }
        if (status != LobbyStatus.OPEN) {
            throw new IllegalInputException("lobby is not open for new players");
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
                seat.setPlayer(new Human(user, this.userService));
                this.numberPlayers++;
                this.numberHumans++;
                break;
            }
        }
        updateLobbyStatus();
        return true;
    }

    public boolean removeUser(String username) {
        return removeUser(userService.getUser(username));
    }

    public boolean removeUser(User user) {
        Seat seatOfUser = seats.stream().filter(
            seat -> seat.getPlayer() != null && seat.getPlayer().getUsername().equals(user.getUsername())
        ).findFirst().orElse(null);

        //user not found
        if (seatOfUser == null) {
            return false;
        }

        seatOfUser.setPlayer(null);
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

    public boolean canBeDeleted() {
        return numberHumans == 0;
    }

    private void selectNewOwner() {
        for (Seat seat : seats) {
            if (seat.getPlayer() instanceof Human) {
                owner = seat.getPlayer();
                return;
            }
        }
    }

    private void updateLobbyStatus() {
        if (numberPlayers == mode.maxPlayer()) {
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
        Seat seatToAddBot = seats.stream().filter(seat -> seat.getPlayer() == null).findFirst().orElse(null);

        //no free seat found
        if (seatToAddBot == null) {
            return false;
        }

        seatToAddBot.setPlayer(new RandomBot("Bot" + seatToAddBot.getId()));
        numberPlayers++;
        updateLobbyStatus();
        return true;
    }

    public boolean changeSeat(int seatId, User user) throws IllegalInputException {
        //check if seatId is valid
        if (seatId < 0 || seatId >= seats.size()) {
            throw new IllegalInputException("seatId is not in bounds");
        }
        //check if seat is already taken
        if (seats.get(seatId).getPlayer() != null) {
            return false;
        }

        //check if user is in the lobby and get the old seat of the user
        Seat oldSeat = seats.stream().filter(
            seat -> seat.getPlayer() != null && seat.getPlayer().getUsername().equals(user.getUsername())
        ).findFirst().orElse(null);

        //user not found
        if (oldSeat == null) {
            throw new IllegalInputException("user is not in lobby");
        }

        seats.get(seatId).setPlayer(oldSeat.getPlayer());
        oldSeat.setPlayer(null);
        return true;

    }

    public boolean changeMode(Mode newMode, User user) throws IllegalInputException {
        if (isNotOwner(user)) {
            throw new IllegalInputException("user is not the owner");
        }
        if (gameManager != null) {
            throw new IllegalInputException("lobby is not in Lobby Screen");
        }
        if (numberPlayers > newMode.maxPlayer()) {
            return false;
        }
        updateLobbyStatus();
        this.mode = newMode;

        //update seats
        //if we have too many seats, move players from the seats that are not in the new mode to empty seats in front
        List<Seat> seatsToMove = new ArrayList<>();
        for (int i = newMode.maxPlayer(); i < seats.size(); i++) {
            if (seats.get(i).getPlayer() != null) {
                seatsToMove.add(seats.get(i));
            }
        }
        //add to remove players to empty seats
        int index = 0;
        for (Seat seat : seatsToMove) {
            //find next empty seat
            while (seats.get(index).getPlayer() != null) {
                index++;
            }
            seats.get(index).setPlayer(seat.getPlayer());
        }

        //remove unnecessary seats at the end
        if (seats.size() > newMode.maxPlayer()) {
            seats = seats.subList(0, newMode.maxPlayer());
        }

        //update identities and add empty seats if necessary
        List<Identity> identities = mode.getIdentityList();
        for (int i = 0; i < mode.maxPlayer(); i++) {
            if (i < seats.size()) {
                seats.get(i).setIdentity(identities.get(i));
            } else {
                //if we have too few seats, add empty seats
                seats.add(new Seat(i, null, identities.get(i)));
            }
        }
        return true;
    }

    public boolean createGame(User user, GameController gameController) throws IllegalInputException {
        if (isNotOwner(user)) {
            throw new IllegalInputException("user is not the owner");
        }

        if (numberPlayers < mode.minPlayer() || numberPlayers > mode.maxPlayer()) {
            return false;
        }
        status = LobbyStatus.CHAMPION_SELECTION;

        List<Player> players = new ArrayList<>(
            seats.stream().map(Seat::getPlayer).filter(Objects::nonNull).toList()
        );

        //randomize positions for identity distribution if mode is Identity (except God King)
        if (mode.isFrom(ModeData.IDENTITY_FOR_EIGHT) || mode.isFrom(ModeData.IDENTITY_FOR_FIVE)) {
            Player godKing = players.remove(0);
            Collections.shuffle(players, Game.RANDOM_SEED);
            players.add(0, godKing);
        }

        //set identities depending on position
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setIdentity(mode.identityList().get(i));
        }

        //randomize positions except God King, God King is always first
        if (mode.isFrom(ModeData.IDENTITY_FOR_EIGHT) || mode.isFrom(ModeData.IDENTITY_FOR_FIVE)) {
            Player godKing = players.remove(0);
            Collections.shuffle(players, Game.RANDOM_SEED);
            players.add(0, godKing);
        } else {
            Collections.shuffle(players, Game.RANDOM_SEED);
        }

        gameManager = new GameManager(players, mode, id, gameController);
        gameManager.gameStart();
        return true;
    }

    private boolean isNotOwner(User user) {
        return !user.getUsername().equals(owner.getUsername());
    }
}

package com.mythostrike.unit;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.model.game.player.Human;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.Identity;
import com.mythostrike.model.lobby.Lobby;
import com.mythostrike.model.lobby.LobbyStatus;
import com.mythostrike.model.lobby.Mode;
import com.mythostrike.model.lobby.ModeList;
import com.mythostrike.model.lobby.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

class LobbyTest {

    private final ModeList modeList = ModeList.getModeList();
    private final User user = new User("Reiner", "Zufall");
    private Lobby lobby;
    @Mock
    private UserService userService;

    @BeforeEach
    void initialize() {
        this.lobby = new Lobby(1, modeList.getMode(0), user, userService);
    }

    @Test
    void testRightInitialization() {
        assertEquals("test mode equal", modeList.getMode(0), lobby.getMode());
        assertEquals("test id equal", 1, lobby.getId());
        assertEquals("test owner equal", new Human(user, userService), lobby.getOwner());
        assertEquals("test status equal", LobbyStatus.OPEN, lobby.getStatus());
        assertEquals("test numberPlayers equal", 1, lobby.getNumberPlayers());
        assertEquals("test numberHumans equal", 1, lobby.getNumberHumans());


        List<Seat> seats = createSeatList(List.of(new Human(user, userService)), modeList.getMode(0));
        assertEquals("test seats", seats, lobby.getSeats());
    }

    @Test
    void testSimpleAddUser() {
        User secondUser = new User("Zweiter", "Benutzer");
        lobby.addUser(secondUser);

        assertEquals("test numberPlayers equal", 2, lobby.getNumberPlayers());
        assertEquals("test numberHumans equal", 2, lobby.getNumberHumans());


        List<Seat> seats = createSeatList(List.of(new Human(user, userService), new Human(secondUser, userService)),
            modeList.getMode(0));
        assertEquals("test seats", seats, lobby.getSeats());
    }

    private List<Seat> createSeatList(List<Player> players, Mode mode) {
        List<Seat> seats = new ArrayList<>(mode.maxPlayer());
        List<Identity> identities = mode.identityList();
        for (int i = 0; i < mode.maxPlayer(); i++) {
            if (i > players.size() - 1) {
                seats.add(new Seat(i, null, identities.get(i)));
            } else {
                seats.add(new Seat(i, players.get(i), identities.get(i)));
            }
        }
        return seats;
    }
}

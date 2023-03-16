package com.mythostrike.unit;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.player.Human;
import com.mythostrike.model.game.player.RandomBot;
import com.mythostrike.model.lobby.Lobby;
import com.mythostrike.model.lobby.LobbyStatus;
import com.mythostrike.model.lobby.ModeList;
import com.mythostrike.model.lobby.Seat;
import com.mythostrike.support.utility.LobbyUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

class LobbyTest {

    private final ModeList modeList = ModeList.getModeList();
    private static final List<User> users = new ArrayList<>();
    private Lobby lobby;
    @Mock
    private UserService userService;

    @BeforeAll
    static void setupUsers() {
        users.add(new User("Reiner", "Zufall"));
        users.add(new User("Zweiter", "Benutzer"));
        users.add(new User("Max", "Mustermann"));
        users.add(new User("Bot3", "Musterfrau"));

    }

    @BeforeEach
    void initialize() {
        this.lobby = new Lobby(1, modeList.getMode(0), users.get(0), userService);
    }

    @Test
    void testRightInitialization() {
        assertEquals("test mode equal", modeList.getMode(0), lobby.getMode());
        assertEquals("test id equal", 1, lobby.getId());
        assertEquals("test owner equal", new Human(users.get(0), userService), lobby.getOwner());
        assertEquals("test status equal", LobbyStatus.OPEN, lobby.getStatus());
        assertEquals("test numberPlayers equal", 1, lobby.getNumberPlayers());
        assertEquals("test numberHumans equal", 1, lobby.getNumberHumans());


        List<Seat> seats = LobbyUtils.createSeatList(List.of(users.get(0)), modeList.getMode(0), userService);
        assertEquals("test seats", seats, lobby.getSeats());
    }

    @Test
    void testJoinAndLeave() {
        lobby.addUser(users.get(1));

        assertEquals("test numberPlayers equal", 2, lobby.getNumberPlayers());
        assertEquals("test numberHumans equal", 2, lobby.getNumberHumans());


        List<Seat> seats = LobbyUtils.createSeatList(List.of(users.get(0), users.get(1)), modeList.getMode(0), userService);
        assertEquals("test seats", seats, lobby.getSeats());

        lobby.removeUser(users.get(1));
        seats = LobbyUtils.createSeatList(List.of(users.get(0)), modeList.getMode(0), userService);
        assertEquals("test seats", seats, lobby.getSeats());

        lobby.addUser(users.get(1));
        assertThrows(IllegalInputException.class, () -> lobby.addUser(users.get(1)));
    }

    @Test
    void testChangeSeat() {
        lobby.addUser(users.get(1));

        assertEquals("test numberPlayers equal", 2, lobby.getNumberPlayers());
        assertEquals("test numberHumans equal", 2, lobby.getNumberHumans());
        List<Seat> seats = LobbyUtils.createSeatList(List.of(users.get(0), users.get(1)),
            modeList.getMode(0), userService);
        assertEquals("test seats", seats, lobby.getSeats());

        lobby.changeSeat(2, users.get(0));
        lobby.addUser(users.get(2));
        seats = LobbyUtils.createSeatList(List.of(users.get(2), users.get(1), users.get(0)),
            modeList.getMode(0), userService);
        assertEquals("test seats", seats, lobby.getSeats());

        assertFalse(lobby.changeSeat(0, users.get(0)));
        lobby.changeSeat(3, users.get(1));
        lobby.addUser(users.get(3));

        seats = LobbyUtils.createSeatList(List.of(users.get(2), users.get(3), users.get(0), users.get(1)),
            modeList.getMode(0), userService);
        assertEquals("test seats", seats, lobby.getSeats());
    }

    @Test
    void changeModeAndBot() {
        lobby.addUser(users.get(1));

        assertEquals("test numberPlayers equal", 2, lobby.getNumberPlayers());
        assertEquals("test numberHumans equal", 2, lobby.getNumberHumans());
        List<Seat> seats = LobbyUtils.createSeatList(List.of(users.get(0), users.get(1)),
            modeList.getMode(0), userService);
        assertEquals("test seats", seats, lobby.getSeats());

        lobby.changeMode(modeList.getMode(1), users.get(0));

        seats = LobbyUtils.createSeatList(List.of(users.get(0), users.get(1)),
            modeList.getMode(1), userService);
        assertEquals("test seats", seats, lobby.getSeats());

        assertThrows(IllegalInputException.class, () -> lobby.changeMode(modeList.getMode(1), users.get(1)));
        assertFalse(lobby.addUser(users.get(2)));


        lobby.changeMode(modeList.getMode(2), users.get(0));
        lobby.addUser(users.get(2));
        seats = LobbyUtils.createSeatList(List.of(users.get(0), users.get(1), users.get(2)),
            modeList.getMode(2), userService);
        assertEquals("test seats", seats, lobby.getSeats());

        assertThrows(IllegalInputException.class, () -> lobby.addBot(users.get(1)));

        lobby.addBot(users.get(0));
        assertTrue(lobby.getSeats().get(3).getPlayer() instanceof RandomBot);
        assertTrue(lobby.getSeats().get(3).getPlayer().getUsername().equals("Bot3"));

        assertFalse(lobby.addBot(users.get(0)));
    }
}

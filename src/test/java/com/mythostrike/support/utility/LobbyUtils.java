package com.mythostrike.support.utility;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.controller.message.lobby.CreateLobbyRequest;
import com.mythostrike.controller.message.lobby.LobbyIdRequest;
import com.mythostrike.controller.message.lobby.LobbyMessage;
import com.mythostrike.controller.message.lobby.SeatMessage;
import com.mythostrike.model.game.player.Human;
import com.mythostrike.model.lobby.Identity;
import com.mythostrike.model.lobby.Lobby;
import com.mythostrike.model.lobby.LobbyList;
import com.mythostrike.model.lobby.Mode;
import com.mythostrike.model.lobby.ModeList;
import com.mythostrike.model.lobby.Seat;
import com.mythostrike.support.SimpleStompFrameHandler;
import com.mythostrike.support.TestUser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
public final class LobbyUtils {
    public static final String WEB_SOCKET_WRONG_MESSAGE = "Web Socket did not receive the correct message";

    private LobbyUtils() {
    }

    public static List<SeatMessage> createSeatMessageList(List<TestUser> players, Mode mode) {
        List<SeatMessage> seats = new ArrayList<>(mode.maxPlayer());
        List<Identity> identities = mode.identityList();
        for (int i = 0; i < mode.maxPlayer(); i++) {
            if (i > players.size() - 1) {
                seats.add(new SeatMessage(identities.get(i)));
            } else {
                seats.add(new SeatMessage(players.get(i).username(), players.get(i).avatarNumber(), identities.get(i)));
            }
        }
        return seats;
    }

    public static List<Seat> createSeatList(List<User> users, Mode mode, UserService userService) {
        List<Seat> seats = new ArrayList<>(mode.maxPlayer());
        List<Identity> identities = mode.identityList();
        for (int i = 0; i < mode.maxPlayer(); i++) {
            if (i > users.size() - 1) {
                seats.add(new Seat(i, null, identities.get(i)));
            } else {
                seats.add(new Seat(i, new Human(users.get(i), userService), identities.get(i)));
            }
        }
        return seats;
    }

    public static void setRandomSeed(int lobbyId, int randomSeed) {
        Lobby lobby = LobbyList.getLobbyList().getLobby(lobbyId);
        if(lobby == null) throw new IllegalArgumentException("Lobby does not exist");
        lobby.setRandomSeed(randomSeed);
    }

    /**
     * Creates a lobby and checks if the response is correct.
     * If the expected status code is not 201, it will try to start the game and expect an error with error message.
     * It will also check if the web socket received the correct message through the frameHandler.
     *
     * @param user               the user that creates the lobby
     * @param modeId             the id of the mode to start with
     * @param expectError        if true, it will try to creat the lobby and expect an error with an error message
     * @param frameHandler       the frameHandler that receives the web socket messages
     * @return the LobbyMessage that was received through the web socket
     */
    public static LobbyMessage createLobby(TestUser user, int modeId, boolean expectError) {
        if (expectError) {
            //try to create the lobby and expect an error with error message
            given()
                .headers(user.headers())
                .body(new CreateLobbyRequest(modeId)).
                when()
                .post("/lobbies").
                then()
                .statusCode(greaterThanOrEqualTo(400))
                .body("message", notNullValue());
            return null;
        }
        Mode mode = ModeList.getModeList().getMode(modeId);
        List<SeatMessage> seatMessageList = createSeatMessageList(List.of(user), mode);

        //create the lobby
        int lobbyId =
            given()
                .headers(user.headers())
                .body(new CreateLobbyRequest(modeId)).
                when()
                .post("/lobbies").
                then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("mode", equalTo(mode.name()))
                .body("owner", equalTo(user.username()))
                .body("seats", hasToString(seatMessageList.toString())).
                extract().body().jsonPath().getInt("id");

        return new LobbyMessage(lobbyId, mode.name(), user.username(), seatMessageList);
    }

    /**
     * Joins a lobby and checks if the response is correct.
     * If the expected status code is not 200, it will try to start the game and expect an error with error message.
     * It will also check if the web socket received the correct message through the frameHandler.
     *
     * @param user               the user that joins the lobby
     * @param oldLobbyState      the old lobby state to compare with the new lobby state
     * @param expectError       if true, it will try to join the lobby and expect an error with an error message
     * @param publicLobbyWebSocket       the frameHandler that receives the web socket messages
     * @return the LobbyMessage that was received through the web socket
     */
    public static LobbyMessage joinLobby(TestUser user, LobbyMessage oldLobbyState, boolean expectError,
                                         SimpleStompFrameHandler<LobbyMessage> publicLobbyWebSocket) {
        assertTrue(publicLobbyWebSocket.getMessages().isEmpty(), "The frame handler should not have any messages");

        if (expectError) {
            //try to join the lobby and expect an error with error message
            given()
                .headers(user.headers())
                .body(new LobbyIdRequest(oldLobbyState.id())).
                when()
                .post("/lobbies/join").
                then()
                .statusCode(greaterThanOrEqualTo(400))
                .body("message", notNullValue());
            return null;
        }
        List<SeatMessage> seatMessageList = oldLobbyState.seats();

        //add player in the first empty seat
        boolean seatFound = false;
        for (int i = 0; i < seatMessageList.size(); i++) {
            if (seatMessageList.get(i).getPlayer() == null) {
                Identity identity = seatMessageList.get(i).getIdentity();
                seatMessageList.set(i, new SeatMessage(user.username(), user.avatarNumber(), identity));
                seatFound = true;
                break;
            }
        }
        if (!seatFound) {
            throw new IllegalArgumentException("No empty seat found in the lobby");
        }

        //join the lobby
        int lobbyId =
            given()
                .headers(user.headers())
                .body(new LobbyIdRequest(oldLobbyState.id())).
                when()
                .post("/lobbies/join").
                then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("mode", equalTo(oldLobbyState.mode()))
                .body("owner", not(equalTo(user.username())))
                .body("seats", hasToString(seatMessageList.toString())).
                extract().body().jsonPath().getInt("id");

        LobbyMessage expected = new LobbyMessage(lobbyId, oldLobbyState.mode(), oldLobbyState.owner(), seatMessageList);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> assertFalse(publicLobbyWebSocket.getMessages().isEmpty()));
        assertEquals(expected, publicLobbyWebSocket.getNextMessage(), WEB_SOCKET_WRONG_MESSAGE);

        return expected;
    }

    /**
     * Starts the game in a lobby and checks if the response is correct.
     * If the expected status code is not 201, it will try to start the game and expect an error with error message.
     *
     * @param user               the user that starts the game
     * @param lobbyId            the id of the lobby
     * @param expectError        if true, it will try to start the game and expect an error with an error message
     * @param expectedStatusCode the expected status code of the response (201 if the game was started successfully)
     */
    public static void startGame(TestUser user, int lobbyId, boolean expectError,
                                 List<SimpleStompFrameHandler<ChampionSelectionMessage>> privateLobbyWebSockets) {
        for (SimpleStompFrameHandler<ChampionSelectionMessage> frameHandler : privateLobbyWebSockets) {
            assertTrue(frameHandler.getMessages().isEmpty(), "The frame handler should not have any messages");
        }


        if (expectError) {
            //try to start the game and expect an error with error message
            given()
                .headers(user.headers())
                .body(new LobbyIdRequest(lobbyId)).
                when()
                .post("/lobbies/start").
                then()
                .statusCode(greaterThanOrEqualTo(400))
                .body("message", notNullValue());
            return;
        }

        //start the game
        given()
            .headers(user.headers())
            .body(new LobbyIdRequest(lobbyId)).
            when()
            .post("/lobbies/start").
            then()
            .statusCode(201);

        await()
            .atMost(2, SECONDS)
            .untilAsserted(() -> {
                //TODO: make message of champion and all other classes who are used for json conversion
                for(SimpleStompFrameHandler<ChampionSelectionMessage> frameHandler : privateLobbyWebSockets) {
                    assertFalse(frameHandler.getMessages().isEmpty());
                }
            });
    }
}

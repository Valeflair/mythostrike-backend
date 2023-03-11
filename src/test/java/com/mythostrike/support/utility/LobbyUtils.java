package com.mythostrike.support.utility;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.lobby.CreateLobbyRequest;
import com.mythostrike.controller.message.lobby.LobbyIdRequest;
import com.mythostrike.controller.message.lobby.LobbyMessage;
import com.mythostrike.controller.message.lobby.SeatMessage;
import com.mythostrike.model.game.player.Human;
import com.mythostrike.model.lobby.Identity;
import com.mythostrike.model.lobby.Mode;
import com.mythostrike.model.lobby.ModeList;
import com.mythostrike.model.lobby.Seat;
import com.mythostrike.support.TestUser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;


@Slf4j
public final class LobbyUtils {
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

    public static LobbyMessage createLobby(TestUser user, int modeId, int expectedStatusCode) {
        if(expectedStatusCode != 201) {
            //create the lobby and expect an error with error message
            given()
                .headers(user.headers())
                .body(new CreateLobbyRequest(modeId)).
                when()
                .post("/lobbies").
                then()
                .statusCode(expectedStatusCode)
                .body("data", notNullValue())
                .body("data.message", notNullValue());
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
                .statusCode(expectedStatusCode)
                .body("id", notNullValue())
                .body("mode", equalTo(mode.name()))
                .body("owner", equalTo(user.username()))
                .body("seats", hasToString(seatMessageList.toString())).
                extract().body().jsonPath().getInt("id");

        return new LobbyMessage(lobbyId, mode.name(), user.username(), seatMessageList);
    }

    public static LobbyMessage joinLobby(TestUser user, LobbyMessage oldLobbyState, int expectedStatusCode) {
        if(expectedStatusCode != 200) {
            //create the lobby and expect an error with error message
            given()
                .headers(user.headers())
                .body(new LobbyIdRequest(oldLobbyState.id())).
                when()
                .post("/lobbies/join").
                then()
                .statusCode(expectedStatusCode)
                .body("data", notNullValue())
                .body("data.message", notNullValue());
            return null;
        }
        List<SeatMessage> seatMessageList = oldLobbyState.seats();

        //add player in the first empty seat
        boolean seatFound = false;
        for(int i = 0; i < seatMessageList.size(); i++) {
            if(seatMessageList.get(i).getPlayer() == null) {
                Identity identity = seatMessageList.get(i).getIdentity();
                seatMessageList.set(i, new SeatMessage(user.username(), user.avatarNumber(), identity));
                seatFound = true;
                break;
            }
        }
        if(!seatFound) {
            throw new IllegalArgumentException("No empty seat found in the lobby");
        }

        //create the lobby
        int lobbyId =
            given()
                .headers(user.headers())
                .body(new LobbyIdRequest(oldLobbyState.id())).
                when()
                .post("/lobbies/join").
                then()
                .statusCode(expectedStatusCode)
                .body("id", notNullValue())
                .body("mode", equalTo(oldLobbyState.mode()))
                .body("owner", not(equalTo(user.username())))
                .body("seats", hasToString(seatMessageList.toString())).
                extract().body().jsonPath().getInt("id");

        return new LobbyMessage(lobbyId, oldLobbyState.mode(), oldLobbyState.owner(), seatMessageList);
    }
}

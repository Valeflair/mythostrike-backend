package com.mythostrike.support.utility;


import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.game.SelectChampionRequest;
import com.mythostrike.controller.message.lobby.*;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.management.PlayerManager;
import com.mythostrike.model.game.player.Champion;
import com.mythostrike.model.game.player.Human;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.*;
import com.mythostrike.support.SimpleStompFrameHandler;
import com.mythostrike.support.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
public final class GameUtils {
    public static final String WEB_SOCKET_WRONG_MESSAGE = "Web Socket did not receive the correct message";


    private GameUtils() {
    }






    public static void selectChampion(TestUser user, int lobbyId, int expectedStatusCode,int championId){
        if (expectedStatusCode != 200) {
            //try to start the game and expect an error with error message
            given()
                    .headers(user.headers())
                    .body(new SelectChampionRequest(lobbyId,championId)).
                    when()
                    .post("/games/play/champion").
                    then()
                    .statusCode(expectedStatusCode)
                    .body("message", notNullValue());
            return;
        }

        given()
                .headers(user.headers())
                .body(new SelectChampionRequest(lobbyId,championId)).
                when()
                .post("/games/play/champion").
                then()
                .statusCode(200);



    }
}
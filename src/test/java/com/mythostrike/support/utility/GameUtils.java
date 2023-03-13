package com.mythostrike.support.utility;


import com.mythostrike.controller.message.game.CardMoveMessage;
import com.mythostrike.controller.message.game.GameMessage;
import com.mythostrike.controller.message.game.GameMessageType;
import com.mythostrike.controller.message.game.PlayCardsRequest;
import com.mythostrike.controller.message.game.PlayerData;
import com.mythostrike.controller.message.game.SelectChampionRequest;
import com.mythostrike.model.game.activity.cards.CardSpaceType;
import com.mythostrike.support.SimpleStompFrameHandler;
import com.mythostrike.support.TestUser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public final class GameUtils {


    private GameUtils() {
    }

    /**
     * Selects a champion for the user and checks if the game update message is received and if the player is in the game.
     * If the expected status code is not 200, it will try to start the game and expect an error with error message.
     *
     * @param user the user that selects the champion
     * @param request the request to select the champion
     * @param selectedChampionName the name of the selected champion
     * @param expectedStatusCode the expected status code
     * @param frameHandlerPublic the frame handler for the public game messages
     */
    public static void selectChampion(TestUser user, SelectChampionRequest request, String selectedChampionName,
                                      int expectedStatusCode, SimpleStompFrameHandler<GameMessage> frameHandlerPublic) {
        assertTrue(frameHandlerPublic.getMessages().isEmpty(), "The frame handler should not have any messages");

        if (expectedStatusCode != 200) {
            //try to start the game and expect an error with error message
            given()
                    .headers(user.headers())
                    .body(request).
                    when()
                    .post("/games/play/champion").
                    then()
                    .statusCode(expectedStatusCode)
                    .body("message", notNullValue());
            return;
        }

        given()
                .headers(user.headers())
                .body(request).
                when()
                .post("/games/play/champion").
                then()
                .statusCode(200);

        await()
            .atMost(2, SECONDS)
            .untilAsserted(() -> {
                    //check if game update message is received
                    assertFalse(frameHandlerPublic.getMessages().isEmpty());
                    GameMessage message = frameHandlerPublic.getMessages().peek();
                    assertEquals(GameMessageType.UPDATE_GAME, message.messageType());
                    assertTrue(message.payload() instanceof Iterable<?>, "The payload should be an iterable");
                    List<PlayerData> playerDataList = new ArrayList<>();
                    for (Object data : (Iterable<?>) message.payload()) {
                        assertTrue(data instanceof PlayerData, "The payload should be a collection of PlayerData");
                        playerDataList.add((PlayerData) data);
                    }

                    //check if the player is in the game
                    Optional<PlayerData> playerData = playerDataList.stream()
                        .filter(data -> data.username().equals(user.username())).findFirst();
                    assertFalse(playerData.isEmpty());
                    assertEquals(selectedChampionName, playerData.get().champion().name());
            });
    }

    public static void playCards(TestUser user, PlayCardsRequest request, int expectedStatusCode,
                                 SimpleStompFrameHandler<GameMessage> frameHandlerPrivate) {
        assertTrue(frameHandlerPrivate.getMessages().isEmpty(), "The frame handler should not have any messages");

        if (expectedStatusCode != 200) {
            //try to start the game and expect an error with error message
            given()
                .headers(user.headers())
                .body(request).
                when()
                .post("/games/play/cards").
                then()
                .statusCode(expectedStatusCode)
                .body("message", notNullValue());
            return;
        }

        given()
            .headers(user.headers())
            .body(request).
            when()
            .post("/games/play/cards").
            then()
            .statusCode(200);

        await()
            .atMost(2, SECONDS)
            .untilAsserted(() -> {
                //check if game update message is received
                assertFalse(frameHandlerPrivate.getMessages().isEmpty());
                GameMessage message = frameHandlerPrivate.getMessages().peek();
                assertEquals(GameMessageType.CARD_MOVE, message.messageType());
                CardMoveMessage cardMoveMessage = (CardMoveMessage) message.payload();

                //check if the right cards are moved
                assertEquals(request.cardIds(), cardMoveMessage.cardIds());
                assertEquals(user.username(), cardMoveMessage.source());
                assertEquals(CardSpaceType.TABLE_PILE.getName(), cardMoveMessage.destination());
            });
    }
}
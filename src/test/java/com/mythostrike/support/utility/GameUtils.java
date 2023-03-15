package com.mythostrike.support.utility;


import com.mythostrike.controller.message.game.*;
import com.mythostrike.controller.message.lobby.LobbyIdRequest;
import com.mythostrike.model.game.activity.cards.CardSpaceType;
import com.mythostrike.support.StompFrameHandlerGame;
import com.mythostrike.support.TestUser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mythostrike.support.utility.UserUtils.I_TEST_USER;
import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public final class GameUtils {

    public static final int WAIT_FOR_HIGHLIGHT_MESSAGE = 5;


    private GameUtils() {
    }

    /**
     * Selects a champion for the user and checks if the game update message is received and if the player is in the game.
     * Removes the game update message from the frame handler.
     * If the expected status code is not 200, it will try to start the game and expect an error with error message.
     *
     * @param user the user that selects the champion
     * @param request the request to select the champion
     * @param selectedChampionName the name of the selected champion
     * @param expectError if the request should expect an error
     * @param publicGameWebSocket the frame handler for the public game messages
     */
    public static void selectChampion(TestUser user, SelectChampionRequest request, String selectedChampionName,
                                      boolean expectError, StompFrameHandlerGame publicGameWebSocket) {
        //TODO: add wait before every Request
        //clear all messages from the frame handler
        publicGameWebSocket.getMessages().clear();

        if (expectError) {
            //try to start the game and expect an error with error message
            given()
                    .headers(user.headers())
                    .body(request).
                    when()
                    .post("/games/play/champion").
                    then()
                    .statusCode(greaterThanOrEqualTo(400))
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
            .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
            .untilAsserted(() -> {
                    //check if game update message is received
                    assertFalse(publicGameWebSocket.getMessages().isEmpty());
            });

        //get update players
        GameMessage message = publicGameWebSocket.getMessages().remove();
        assertEquals(message.messageType(), GameMessageType.UPDATE_GAME, "wrong message type");

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
    }

    public static void playCards(TestUser user, PlayCardsRequest request, boolean expectError,
                                 StompFrameHandlerGame privateGameWebSocket) {
        //clear all messages from the frame handler
        privateGameWebSocket.getMessages().clear();

        if (expectError) {
            //try to start the game and expect an error with error message
            given()
                .headers(user.headers())
                .body(request).
                when()
                .post("/games/play/cards").
                then()
                .statusCode(greaterThanOrEqualTo(400))
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

        //if the cardIds is empty, we dont get a card move message
        if (request.cardIds().isEmpty()) {
            return;
        }

        await()
            .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
            .untilAsserted(() -> {
                //check if game update message is received
                assertFalse(privateGameWebSocket.getMessages().isEmpty());
            });

        GameMessage message = privateGameWebSocket.getMessages().peek();
        assertEquals(GameMessageType.CARD_MOVE, message.messageType());
        CardMoveMessage cardMoveMessage = (CardMoveMessage) message.payload();

        //check if the right cards are moved
        assertEquals(request.cardIds(), cardMoveMessage.cardIds());
        assertEquals(user.username(), cardMoveMessage.source());
    }

    public static void useSkill(TestUser user, UseSkillRequest request, boolean expectError,
                                StompFrameHandlerGame publicGameWebSocket) {
        //clear all messages from the frame handler
        publicGameWebSocket.getMessages().clear();

        if (expectError) {
            //try to start the game and expect an error with error message
            given()
                    .headers(user.headers())
                    .body(request).
                    when()
                    .post("/games/play/skills").
                    then()
                    .statusCode(greaterThanOrEqualTo(400))
                    .body("message", notNullValue());
            return;
        }

        given()
                .headers(user.headers())
                .body(request).
                when()
                .post("/games/play/skills").
                then()
                .statusCode(200);

        await()
                .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
                .untilAsserted(() -> {
                    //check if websocket message is received
                    assertFalse(publicGameWebSocket.getMessages().isEmpty());
                });
    }

    public static void endTurn(TestUser user, int lobbyId, boolean expectError, boolean hasToCleanTable,
                               boolean hasToDiscardCards, StompFrameHandlerGame privateGameWebSocket) {
        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //clear all messages from the frame handler
        privateGameWebSocket.getMessages().clear();

        if (expectError) {
            //try to start the game and expect an error with error message
            given()
                    .headers(user.headers())
                    .body(new LobbyIdRequest(lobbyId)).
                    when()
                    .post("/games/play/end").
                    then()
                    .statusCode(greaterThanOrEqualTo(400))
                    .body("message", notNullValue());
            return;
        }

        given()
                .headers(user.headers())
                .body(new LobbyIdRequest(lobbyId)).
                when()
                .post("/games/play/end").
                then()
                .statusCode(200);

        if(hasToCleanTable) {
            await()
                .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
                .untilAsserted(() -> {
                    //check if card move message from table pile to discard pile is received
                    assertFalse(privateGameWebSocket.getMessages().isEmpty());
                    GameMessage message = privateGameWebSocket.getMessages().remove();

                    CardMoveMessage cardMoveMessage = (CardMoveMessage) message.payload();

                    //check if the right cards are moved
                    //assertEquals(request.cardIds(), cardMoveMessage.cardIds());
                    assertEquals(CardSpaceType.TABLE_PILE.getName(), cardMoveMessage.source());
                    assertEquals(CardSpaceType.DISCARD_PILE.getName(), cardMoveMessage.destination());
                });
        }
        if (hasToDiscardCards) {
            await()
                    .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
                    .untilAsserted(() -> {
                        //check if game update message is received
                        assertFalse(privateGameWebSocket.getMessages().isEmpty());
                    });
            GameMessage message = privateGameWebSocket.getMessages().peek();
            assertEquals(message.messageType(), GameMessageType.HIGHLIGHT, "Received wrong message type: " + message.messageType());
        }
    }


    public static void playCardOnTarget(TestUser user, int lobbyId, int cardId, String target, StompFrameHandlerGame privateGameWebSocket) {

        //wait for the next pick request players highlight message
        await()
                .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
                .untilAsserted(
                        () -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        PlayCardsRequest playCardsRequest = new PlayCardsRequest(lobbyId, List.of(cardId), List.of(target));
        GameUtils.playCards(user, playCardsRequest, false, privateGameWebSocket);
    }

    public static void playCardOnTargetList(TestUser user, int lobbyId, int cardId, List<String> targets, StompFrameHandlerGame privateGameWebSocket) {
        //wait for the next pick request players highlight message
        await()
                .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
                .untilAsserted(
                        () -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        PlayCardsRequest playCardsRequest = new PlayCardsRequest(lobbyId, List.of(cardId), targets);
        GameUtils.playCards(user, playCardsRequest, false, privateGameWebSocket);
    }

    public static void playCard(TestUser user, int lobbyId, int cardId, StompFrameHandlerGame privateGameWebSocket) {
        //wait for the next pick request players highlight message
        await()
                .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
                .untilAsserted(
                        () -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        PlayCardsRequest playCardsRequest = new PlayCardsRequest(lobbyId, List.of(cardId), List.of());
        GameUtils.playCards(user, playCardsRequest, false, privateGameWebSocket);
    }

    public static void playMultipleCards(TestUser user, int lobbyId, List<Integer> cardIds, StompFrameHandlerGame privateGameWebSocket) {
        //wait for the next pick request players highlight message
        await()
                .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
                .untilAsserted(
                        () -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        PlayCardsRequest playCardsRequest = new PlayCardsRequest(lobbyId, cardIds, List.of());
        GameUtils.playCards(user, playCardsRequest, false, privateGameWebSocket);
    }

    public static void confirm(TestUser user, int lobbyId,StompFrameHandlerGame privateGameWebSocket) {
        //wait for the next pick request players highlight message
        await()
                .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
                .untilAsserted(
                        () -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        PlayCardsRequest playCardsRequest = new PlayCardsRequest(lobbyId, List.of(), List.of());
        GameUtils.playCards(user, playCardsRequest, false, privateGameWebSocket);
    }

    public static void discardCard(TestUser user, int lobbyId, List<Integer> cardId, StompFrameHandlerGame privateGameWebSocket) {
        //wait for the next pick request players highlight message
        await()
                .atMost(WAIT_FOR_HIGHLIGHT_MESSAGE, SECONDS)
                .untilAsserted(
                        () -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        PlayCardsRequest playCardsRequest = new PlayCardsRequest(lobbyId, cardId, List.of());
        GameUtils.playCards(user, playCardsRequest, false, privateGameWebSocket);
    }


}
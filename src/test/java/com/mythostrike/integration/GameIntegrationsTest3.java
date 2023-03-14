package com.mythostrike.integration;


import com.mythostrike.controller.message.game.GameMessageType;
import com.mythostrike.controller.message.game.PlayCardsRequest;
import com.mythostrike.controller.message.game.SelectChampionRequest;
import com.mythostrike.controller.message.game.UseSkillRequest;
import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.controller.message.lobby.LobbyMessage;
import com.mythostrike.model.game.player.ChampionList;
import com.mythostrike.support.SimpleStompFrameHandler;
import com.mythostrike.support.StompFrameHandlerGame;
import com.mythostrike.support.TestUser;
import com.mythostrike.support.utility.GameUtils;
import com.mythostrike.support.utility.LobbyUtils;
import com.mythostrike.support.utility.UserUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.mythostrike.support.utility.LobbyUtils.WEB_SOCKET_WRONG_MESSAGE;
import static com.mythostrike.support.utility.UserUtils.I_JACK;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Slf4j
class GameIntegrationTest3 {
    private static final Integer PORT = 8080;
    public static final int I_TEST_USER = 0;
    public static final int I_REINER_ZUFALL = 1;
    public static final int I_MINH = 2;
    public static final int I_JACK = 3;
    public static final int I_TILL = 4;
    private final List<TestUser> users = new ArrayList<>();
    private StompSession session;

    @BeforeAll
    static void restAssuredSetup() {
        RestAssured.requestSpecification = new RequestSpecBuilder().build()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON);
        RestAssured.port = PORT;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void webSocketSetup() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(new SockJsClient(
            List.of(new WebSocketTransport(new StandardWebSocketClient()))));

        String webSocketPath = "ws://localhost:" + PORT + "/updates";
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.session = webSocketStompClient
            .connectAsync(webSocketPath, new StompSessionHandlerAdapter() {
            })
            .get(1, SECONDS);
    }

    //TODO: mock jwt Token generation or database for tests

    /**
     * before using the users, we need to create them and get their jwt tokens.
     * If the user does not exist, we register them.
     * Cannot be done in @BeforeAll because the server needs to be running.
     */
    @BeforeEach
    void setupUsers() {
        users.addAll(UserUtils.getInstance().getUsers(4));
    }


    /**
     * Integration tests if we create a lobby and subscribe to it via websockets
     *
     * @throws Exception
     */
    @Test
    void gameIdentityForFive() {
        int lobbyId = createLobbyAndStartGame();

        //subscribe to the public game Web Socket
        StompFrameHandlerGame publicGameWebSocket = new StompFrameHandlerGame();
        //wait for update game message and remove it from the queue
        session.subscribe(String.format("/games/%d", lobbyId) , publicGameWebSocket);
        try{
            sleep(200);
        } catch (InterruptedException e) {
            log.debug("Interrupted while sleeping");
        }

        selectChampions(lobbyId, publicGameWebSocket);

        //connect to all private game web Sockets
        List<StompFrameHandlerGame> privateGameWebSockets = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            privateGameWebSockets.add(new StompFrameHandlerGame());
            session.subscribe(String.format("/games/%d/%s",  lobbyId, users.get(i).username()), privateGameWebSockets.get(i));
        }

    }

    private int createLobbyAndStartGame() {
        //create the lobby
        LobbyMessage expected = LobbyUtils.createLobby(users.get(I_TEST_USER), 0, false);

        int lobbyId = expected.id();

        //subscribe to the lobby
        SimpleStompFrameHandler<LobbyMessage> publicLobbyWebSocket = new SimpleStompFrameHandler<>(LobbyMessage.class);
        session.subscribe(String.format("/lobbies/%d", lobbyId) , publicLobbyWebSocket);
        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> assertFalse(publicLobbyWebSocket.getMessages().isEmpty()));
        assertEquals(expected, publicLobbyWebSocket.getNextMessage(), WEB_SOCKET_WRONG_MESSAGE);

        List<SimpleStompFrameHandler<ChampionSelectionMessage>> privateLobbyWebSockets = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            privateLobbyWebSockets.add(new SimpleStompFrameHandler<>(ChampionSelectionMessage.class));
            session.subscribe(String.format("/lobbies/%d/%s",  lobbyId, users.get(i).username()), privateLobbyWebSockets.get(i));
        }

        //change the random seed of the lobby
        LobbyUtils.setRandomSeed(lobbyId, 9527);
        //join the lobby
        expected = LobbyUtils.joinLobby(users.get(I_REINER_ZUFALL), expected, false, publicLobbyWebSocket);
        assertNotNull(expected);
        expected = LobbyUtils.joinLobby(users.get(I_MINH), expected, false, publicLobbyWebSocket);
        assertNotNull(expected);
        expected = LobbyUtils.joinLobby(users.get(I_JACK), expected, false, publicLobbyWebSocket);
        assertNotNull(expected);



        //TODO:add bot


        //start the game
        LobbyUtils.startGame(users.get(I_TEST_USER), lobbyId, false, privateLobbyWebSockets);

        for (int i = 0; i < users.size(); i++) {
            int finalI = i;
            await()
                .atMost(1, SECONDS)
                .untilAsserted(() -> {
                    assertFalse(privateLobbyWebSockets.get(finalI).getMessages().isEmpty());
                });
            assertEquals(ChampionSelectionMessage.class, privateLobbyWebSockets.get(i).getNextMessage().getClass(),
                WEB_SOCKET_WRONG_MESSAGE);
        }
        return lobbyId;
    }


    private void selectChampions(int lobbyId, StompFrameHandlerGame publicGameWebSocket) {
        //select champions for each user
        int championId = 2;
        String championName = ChampionList.getChampionList().getChampion(championId).getName();
        GameUtils.selectChampion(users.get(I_TEST_USER), new SelectChampionRequest(lobbyId, championId), championName,
            false, publicGameWebSocket);

        championId = 6;
        championName = ChampionList.getChampionList().getChampion(championId).getName();
        GameUtils.selectChampion(users.get(I_REINER_ZUFALL), new SelectChampionRequest(lobbyId, championId), championName,
            false, publicGameWebSocket);

        championId = 3;
        championName = ChampionList.getChampionList().getChampion(championId).getName();
        GameUtils.selectChampion(users.get(I_MINH), new SelectChampionRequest(lobbyId, championId), championName,
            false, publicGameWebSocket);

        championId = 1;
        championName = ChampionList.getChampionList().getChampion(championId).getName();
        GameUtils.selectChampion(users.get(I_JACK), new SelectChampionRequest(lobbyId, championId), championName,
            false, publicGameWebSocket);
    }


    private void round1Jack(int lobbyId, StompFrameHandlerGame privateGameWebSocket) {
        TestUser currentPlayer = users.get(I_JACK);

        GameUtils.playCard(currentPlayer, lobbyId, 1035, privateGameWebSocket);
    }
}

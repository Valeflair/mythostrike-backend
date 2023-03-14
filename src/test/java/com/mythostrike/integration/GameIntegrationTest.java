package com.mythostrike.integration;


import com.mythostrike.controller.message.game.GameMessage;
import com.mythostrike.controller.message.game.SelectChampionRequest;
import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.controller.message.lobby.LobbyMessage;
import com.mythostrike.model.game.player.ChampionList;
import com.mythostrike.support.SimpleStompFrameHandler;
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
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Slf4j
class GameIntegrationTest {

    private static final Integer PORT = 8080;
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
        int id = 0;
        users.add(UserUtils.createUser("TestUser", "TestPassword"));
        assertFalse(users.get(id++).jwtToken().isEmpty());

        users.add(UserUtils.createUser("Reiner Zufall", "12341234"));
        assertFalse(users.get(id++).jwtToken().isEmpty());

        users.add(UserUtils.createUser("Minh-Trung Minh-Trung Tang", "MinhTrungTangMinhTrungTang"));
        assertFalse(users.get(id++).jwtToken().isEmpty());

        users.add(UserUtils.createUser("__Jack__", "JackyChanJackyChan"));
        assertFalse(users.get(id++).jwtToken().isEmpty());

        /*users.add(UserUtils.createUser("Till1234", "IchBinEinCoolesPassword"));
        assertFalse(users.get(id++).jwtToken().isEmpty());*/
    }


    /**
     * Integration tests if we create a lobby and subscribe to it via websockets
     *
     * @throws Exception
     */
    @Test
    void gameTwoVsTwoTest() {
        //create the lobby
        LobbyMessage expected = LobbyUtils.createLobby(users.get(0), 0, false);

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
        LobbyUtils.setRandomSeed(lobbyId, 300); //TODO: change to what seed you want

        //join the lobby
        expected = LobbyUtils.joinLobby(users.get(1), expected, false, publicLobbyWebSocket);
        assertNotNull(expected);
        expected = LobbyUtils.joinLobby(users.get(2), expected, false, publicLobbyWebSocket);
        assertNotNull(expected);
        expected = LobbyUtils.joinLobby(users.get(3), expected, false, publicLobbyWebSocket);
        assertNotNull(expected);

        //start the game
        LobbyUtils.startGame(users.get(0), lobbyId, false, privateLobbyWebSockets);

        //TODO: check if for each user the champion selection message is received

        //subscribe to the lobby
        SimpleStompFrameHandler<GameMessage> publicGameWebSocket = new SimpleStompFrameHandler<>(GameMessage.class);
        //wait for update game message and remove it from the queue
        session.subscribe(String.format("/games/%d", expected.id()) , publicGameWebSocket);
        try{
            sleep(200);
        } catch (InterruptedException e) {
            log.debug("Interrupted while sleeping");
        }

        List<SimpleStompFrameHandler<GameMessage>> privateGameWebSockets = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            privateGameWebSockets.add(new SimpleStompFrameHandler<>(GameMessage.class));
            session.subscribe(String.format("/games/%d/%s",  expected.id(), users.get(i).username()), privateGameWebSockets.get(i));
        }

        //TODO: select champion for every player
        int championId = 1;
        String championName = ChampionList.getChampionList().getChampion(championId).getName();
        GameUtils.selectChampion(users.get(0), new SelectChampionRequest(lobbyId, championId), championName,
            false, publicGameWebSocket);


        //TODO: play game


    }
}

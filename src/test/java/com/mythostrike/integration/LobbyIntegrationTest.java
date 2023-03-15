package com.mythostrike.integration;


import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.controller.message.lobby.LobbyMessage;
import com.mythostrike.support.SimpleStompFrameHandler;
import com.mythostrike.support.TestUser;
import com.mythostrike.support.utility.LobbyUtils;
import com.mythostrike.support.utility.UserUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
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
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class LobbyIntegrationTest {

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


    /**
     * Integration tests if we create a lobby and subscribe to it via websockets
     *
     * @throws Exception
     */
    @Test
    void testLobbyTwoPlayer() {
        users.clear();
        users.addAll(UserUtils.getInstance().getUsers(2));

        //create the lobby
        LobbyMessage expected = LobbyUtils.createLobby(users.get(0), 0, false);

        //subscribe to the lobby
        SimpleStompFrameHandler<LobbyMessage> publicWebSocket = new SimpleStompFrameHandler<>(LobbyMessage.class);
        session.subscribe("/lobbies/" + expected.id(), publicWebSocket);
        await()
            .atMost(2, SECONDS)
            .untilAsserted(() -> assertFalse(publicWebSocket.getMessages().isEmpty()));
        assertEquals(expected, publicWebSocket.getNextMessage(), WEB_SOCKET_WRONG_MESSAGE);

        List<SimpleStompFrameHandler<ChampionSelectionMessage>> privateWebSockets = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            privateWebSockets.add(new SimpleStompFrameHandler<>(ChampionSelectionMessage.class));
            session.subscribe(String.format("/lobbies/%d/%s", expected.id(), users.get(i).username()),
                privateWebSockets.get(i));
        }

        //change the random seed of the lobby
        LobbyUtils.setRandomSeed(expected.id(), 42); //TODO: change to what seed you want

        //join the lobby again, expect error
        LobbyUtils.joinLobby(users.get(0), expected, true, publicWebSocket);

        //join lobby
        expected = LobbyUtils.joinLobby(users.get(1), expected, false, publicWebSocket);


        //leave lobby directly after joining
        expected = LobbyUtils.leaveLobby(users.get(1), expected, false, publicWebSocket);


        //leave lobby again, expect error
        expected = LobbyUtils.leaveLobby(users.get(1), expected, true, publicWebSocket);

        //join lobby again
        expected = LobbyUtils.joinLobby(users.get(1), expected, false, publicWebSocket);


        //not owner cant change lobby
        expected = LobbyUtils.changeMode(users.get(1), expected, 1, true, publicWebSocket);

        //change mode to 1 vs 1
        expected = LobbyUtils.changeMode(users.get(0), expected, 1, false, publicWebSocket);


        //add Bot, lobby full
        expected = LobbyUtils.addBot(users.get(0), expected, true, publicWebSocket);

        //change mode to 2 vs 2
        expected = LobbyUtils.changeMode(users.get(0), expected, 2, false, publicWebSocket);

        //add Bot not owner
        expected = LobbyUtils.addBot(users.get(1), expected, true, publicWebSocket);

        //add Bot
        expected = LobbyUtils.addBot(users.get(0), expected, false, publicWebSocket);

        //start game not owner
        LobbyUtils.startGame(users.get(1), expected.id(), true, privateWebSockets);

        //start game not enough players
        LobbyUtils.startGame(users.get(0), expected.id(), true, privateWebSockets);

        //change seat to invalid seat
        expected = LobbyUtils.changeSeat(users.get(0), expected, 4, true, publicWebSocket);

        //change seat to empty seat
        expected = LobbyUtils.changeSeat(users.get(0), expected, 3, false, publicWebSocket);

        //change seat to occupied seat
        expected = LobbyUtils.changeSeat(users.get(0), expected, 1, true, publicWebSocket);

        //add Bot
        expected = LobbyUtils.addBot(users.get(0), expected, false, publicWebSocket);

        //add more Bots then allowed
        expected = LobbyUtils.addBot(users.get(0), expected, true, publicWebSocket);

        //start the game
        LobbyUtils.startGame(users.get(0), expected.id(), false, privateWebSockets);
    }

    @Test
    void testLobbyEightPlayer() {
        users.clear();
        users.addAll(UserUtils.getInstance().getUsers(7));

        //create the lobby
        LobbyMessage expected = LobbyUtils.createLobby(users.get(0), 0, false);

        //subscribe to the lobby
        SimpleStompFrameHandler<LobbyMessage> publicWebSocket = new SimpleStompFrameHandler<>(LobbyMessage.class);
        session.subscribe("/lobbies/" + expected.id(), publicWebSocket);
        await()
            .atMost(2, SECONDS)
            .untilAsserted(() -> assertFalse(publicWebSocket.getMessages().isEmpty()));
        assertEquals(expected, publicWebSocket.getNextMessage(), WEB_SOCKET_WRONG_MESSAGE);

        List<SimpleStompFrameHandler<ChampionSelectionMessage>> privateWebSockets = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            privateWebSockets.add(new SimpleStompFrameHandler<>(ChampionSelectionMessage.class));
            session.subscribe(String.format("/lobbies/%d/%s", expected.id(), users.get(i).username()),
                privateWebSockets.get(i));
        }

        //change the random seed of the lobby
        LobbyUtils.setRandomSeed(expected.id(), 42); //TODO: change to what seed you want

        //join the lobby again, expect error
        LobbyUtils.joinLobby(users.get(0), expected, true, publicWebSocket);

        //join lobby
        expected = LobbyUtils.joinLobby(users.get(1), expected, false, publicWebSocket);
        expected = LobbyUtils.joinLobby(users.get(2), expected, false, publicWebSocket);
        expected = LobbyUtils.joinLobby(users.get(3), expected, false, publicWebSocket);
        expected = LobbyUtils.joinLobby(users.get(4), expected, false, publicWebSocket);
        expected = LobbyUtils.joinLobby(users.get(5), expected, false, publicWebSocket);
        expected = LobbyUtils.joinLobby(users.get(6), expected, false, publicWebSocket);

        //add Bot
        expected = LobbyUtils.addBot(users.get(0), expected, false, publicWebSocket);

        //add more Bots then allowed
        expected = LobbyUtils.addBot(users.get(0), expected, true, publicWebSocket);

        //change mode to 1 vs 1, expect error
        expected = LobbyUtils.changeMode(users.get(0), expected, 1, true, publicWebSocket);

        //change to Identity for 8, expect error
        expected = LobbyUtils.changeMode(users.get(0), expected, 7, true, publicWebSocket);


        //start the game
        LobbyUtils.startGame(users.get(0), expected.id(), false, privateWebSockets);
    }
}

package com.mythostrike.integration;


import com.mythostrike.controller.message.game.GameMessage;
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

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    //TODO: mock jwt Token generation or database for tests

    /**
     * before using the users, we need to create them and get their jwt tokens.
     * If the user does not exist, we register them.
     * Cannot be done in @BeforeAll because the server needs to be running.
     */
    @BeforeEach
    void setupUsers() {
        users.add(UserUtils.createUser("TestUser", "TestPassword"));
        assertFalse(users.get(0).jwtToken().isEmpty());

        users.add(UserUtils.createUser("Reiner Zufall", "12341234"));
        assertFalse(users.get(1).jwtToken().isEmpty());
    }


    /**
     * Integration tests if we create a lobby and subscribe to it via websockets
     *
     * @throws Exception
     */
    @Test
    void testLobbyTwoPlayer() {
        //create the lobby
        LobbyMessage expected = LobbyUtils.createLobby(users.get(0), 0, 201);

        //subscribe to the lobby
        SimpleStompFrameHandler<LobbyMessage> frameHandlerPublic = new SimpleStompFrameHandler<>(LobbyMessage.class);
        session.subscribe("/lobbies/" + expected.id() , frameHandlerPublic);

        List<SimpleStompFrameHandler<ChampionSelectionMessage>> frameHandlersPrivate = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            frameHandlersPrivate.add(new SimpleStompFrameHandler<>(ChampionSelectionMessage.class));
            session.subscribe(String.format("/lobbies/%d/%s",  expected.id(), users.get(i).username()), frameHandlersPrivate.get(i));
        }

        //join the lobby
        expected = LobbyUtils.joinLobby(users.get(1), expected, 200, frameHandlerPublic);
        assertNotNull(expected);

        //join the lobby again, expect error
        LobbyUtils.joinLobby(users.get(0), expected, 400, frameHandlerPublic);

        //start the game
        LobbyUtils.startGame(users.get(0), expected.id(), 201, frameHandlersPrivate);
    }
}

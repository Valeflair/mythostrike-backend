package com.mythostrike.integration;


import com.mythostrike.controller.message.lobby.CreateLobbyRequest;
import com.mythostrike.model.lobby.ModeList;
import com.mythostrike.support.LobbyData;
import com.mythostrike.support.SimpleStompFrameHandler;
import com.mythostrike.support.TestUser;
import com.mythostrike.support.UserUtils;
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

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class GameRunIntegrationTest {

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
    void testLobbyWebSocketConnection() {
        //create the lobby
        given()
            .headers(users.get(0).headers())
            .body(new CreateLobbyRequest(0)).
            when()
            .post("/lobbies").
            then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("mode", equalTo(ModeList.getModeList().getMode(0).name()))
            .body("owner", equalTo(users.get(0).username()));


        //subscribe to the lobby
        SimpleStompFrameHandler<LobbyData> frameHandler = new SimpleStompFrameHandler<>(LobbyData.class);
        session.subscribe("/lobbies/1", frameHandler);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> assertFalse(frameHandler.getMessages().isEmpty()));
    }
}

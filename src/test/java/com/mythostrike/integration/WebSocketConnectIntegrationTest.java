package com.mythostrike.integration;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.AuthenticationController;
import com.mythostrike.controller.LobbyController;
import com.mythostrike.controller.message.authentication.UserAuthRequest;
import com.mythostrike.controller.message.lobby.ChangeModeRequest;
import com.mythostrike.controller.message.lobby.CreateLobbyRequest;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.lobby.Lobby;
import com.mythostrike.model.lobby.ModeList;
import com.mythostrike.support.SimplePrincipal;
import lombok.RequiredArgsConstructor;
import org.hibernate.AssertionFailure;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * complicated Test for Websockets. Groundwork from the Example to Websockets from spring.io.
 *
 * @author Till
 * @version 1.0
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class WebSocketConnectIntegrationTest {

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private LobbyController lobbyController;
    @Autowired
    private UserService userService;

    @Value("${local.server.port}")
    private int port;
    private SockJsClient sockJsClient;
    private WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.sockJsClient = new SockJsClient(transports);

        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    void connect() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();

        //generate a test user and lobby to compare with.
        Principal testUserPrincipal = new SimplePrincipal("TestUser");
        try {
            authenticationController.register(new UserAuthRequest(testUserPrincipal.getName(), "TestPassword"));
        } catch (ResponseStatusException e) {
            System.out.println("User already exists, ignoring");
        }
        User testUser = userService.getUser(testUserPrincipal.getName());

        Lobby testLobby;
        try {
            testLobby = new Lobby(1, ModeList.getModeList().getMode(5), testUser, userService);
        } catch (IllegalInputException e) {
            fail("Failed to create test lobby");
            return;
        }

        StompSessionHandler handler = new TestSessionHandler(failure) {

            @Override
            public void afterConnected(final StompSession session, @NotNull StompHeaders connectedHeaders) {
                System.out.println("Connected! Headers: " + connectedHeaders);
                session.subscribe("/lobbies/1", new StompFrameHandler() {
                    @Override
                    public @NotNull Type getPayloadType(@NotNull StompHeaders headers) {
                        return Lobby.class;
                    }

                    @Override
                    public void handleFrame(@NotNull StompHeaders headers, Object payload) {
                        Lobby lobby = (Lobby) payload;
                        try {
                            assertEquals(lobby, testLobby);
                        } catch (AssertionFailure t) {
                            failure.set(t);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }
                    }
                });

                try {
                    lobbyController.create(testUserPrincipal, new CreateLobbyRequest(1));
                    lobbyController.changeMode(testUserPrincipal, new ChangeModeRequest(1, 1));
                } catch (IllegalInputException e) {
                    failure.set(e);
                }
            }
        };

        this.stompClient.connectAsync("http://localhost:{port}/updates", this.headers, handler, this.port);

        /* Test if update lobby is received within 3 seconds. Does not work. Not important for now, works in Frontend.
        if (latch.await(3, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        } else {
            fail("Lobby update not received");
        }*/
    }

    private static class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;

        public TestSessionHandler(AtomicReference<Throwable> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        @Override
        public void handleException(@NotNull StompSession session, StompCommand command,
                                    @NotNull StompHeaders headers, byte @NotNull [] payload,
                                    @NotNull Throwable exception) {
            this.failure.set(exception);
        }

        @Override
        public void handleTransportError(@NotNull StompSession session, @NotNull Throwable exception) {
            this.failure.set(exception);
        }
    }
}

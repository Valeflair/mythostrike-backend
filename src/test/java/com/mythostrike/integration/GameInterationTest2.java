package com.mythostrike.integration;


import com.mythostrike.controller.message.game.GameMessageType;
import com.mythostrike.controller.message.game.SelectChampionRequest;
import com.mythostrike.controller.message.game.UseSkillRequest;
import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.controller.message.lobby.LobbyMessage;
import com.mythostrike.model.game.Game;
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
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Slf4j
class Game2IntegrationTest {
    private static final Integer PORT = 8080;
    private static final int I_ACHILLES = 0;
    private static final int I_HESTIA = 1;
    private static final int I_TERPISORE = 2;
    private static final int I_KRATOS = 3;
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
    void wholeGameWith2Vs2() {
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

        round1Kratos(lobbyId, privateGameWebSockets);
        round2Achilles(lobbyId, privateGameWebSockets);
        round3Hestia(lobbyId,privateGameWebSockets);
        round4Terpisore(lobbyId,privateGameWebSockets);
        round5Kratos(lobbyId,privateGameWebSockets);
        round6Achilles(lobbyId,privateGameWebSockets);
        round7Hestia(lobbyId,privateGameWebSockets);
        round8Terpisore(lobbyId,privateGameWebSockets);
        round9Kratos(lobbyId,privateGameWebSockets);
        //round10Achilles gets skipped, because of nightmare
        round11Hestia(lobbyId,privateGameWebSockets);
        round12Terpisore(lobbyId,privateGameWebSockets);
        round13Kratos(lobbyId,privateGameWebSockets);
        round14Achilles(lobbyId,privateGameWebSockets);
        round15Hestia(lobbyId,privateGameWebSockets);
    }

    private int createLobbyAndStartGame() {
        //create the lobby
        LobbyMessage expected = LobbyUtils.createLobby(users.get(I_ACHILLES), 0, false);

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
        expected = LobbyUtils.joinLobby(users.get(I_HESTIA), expected, false, publicLobbyWebSocket);

        expected = LobbyUtils.joinLobby(users.get(I_TERPISORE), expected, false, publicLobbyWebSocket);

        expected = LobbyUtils.joinLobby(users.get(I_KRATOS), expected, false, publicLobbyWebSocket);


        expected = LobbyUtils.changeMode(users.get(I_ACHILLES), expected, 2, false, publicLobbyWebSocket);


        //start the game
        LobbyUtils.startGame(users.get(I_ACHILLES), lobbyId, false, privateLobbyWebSockets);

        //TODO: check if for each user the champion selection message is received
        return lobbyId;
    }

    private void selectChampions(int lobbyId, StompFrameHandlerGame publicGameWebSocket) {
        //select champions for each user
        int championId = 1;
        String championName = ChampionList.getChampionList().getChampion(championId).getName();
        GameUtils.selectChampion(users.get(I_ACHILLES), new SelectChampionRequest(lobbyId, championId), championName,
                false, publicGameWebSocket);

        championId = 6;
        championName = ChampionList.getChampionList().getChampion(championId).getName();
        GameUtils.selectChampion(users.get(I_HESTIA), new SelectChampionRequest(lobbyId, championId), championName,
                false, publicGameWebSocket);

        championId = 3;
        championName = ChampionList.getChampionList().getChampion(championId).getName();
        GameUtils.selectChampion(users.get(I_TERPISORE), new SelectChampionRequest(lobbyId, championId), championName,
                false, publicGameWebSocket);

        championId = 4;
        championName = ChampionList.getChampionList().getChampion(championId).getName();
        GameUtils.selectChampion(users.get(I_KRATOS), new SelectChampionRequest(lobbyId, championId), championName,
                false, publicGameWebSocket);
    }

    private void round1Kratos(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_KRATOS);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));


        //play extort
        GameUtils.playCardOnTarget(users.get(I_KRATOS), lobbyId, 1057, users.get(I_HESTIA).username(),
                privateGameWebSocketList.get(I_KRATOS));


        //play attack
        GameUtils.playCardOnTarget(users.get(I_KRATOS), lobbyId, 1025, users.get(I_HESTIA).username(),
                privateGameWebSocketList.get(I_KRATOS));

        //play defend from Hestia
        GameUtils.playCard(users.get(I_HESTIA), lobbyId, 1049,
                privateGameWebSocketList.get(I_HESTIA));

        //Godarena
        GameUtils.playCard(users.get(I_KRATOS), lobbyId, 1005,
                privateGameWebSocketList.get(I_KRATOS));

        //play attack from Achilles
        GameUtils.playCard(users.get(I_ACHILLES), lobbyId, 1021,
                privateGameWebSocketList.get(I_ACHILLES));

        //skip from Hestia
        GameUtils.confirm(users.get(I_HESTIA), lobbyId,
                privateGameWebSocketList.get(I_HESTIA));

        //play attack from terpisore
        GameUtils.playCard(users.get(I_TERPISORE), lobbyId, 1012,
                privateGameWebSocketList.get(I_TERPISORE));

        //end turn
        GameUtils.endTurn(users.get(I_KRATOS), lobbyId, false, true, false, privateGameWebSocket);

    }

    private void round2Achilles(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_ACHILLES);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play Drought on Kratos
        GameUtils.playCardOnTarget(users.get(I_ACHILLES), lobbyId, 1073, users.get(I_KRATOS).username(),
                privateGameWebSocketList.get(I_ACHILLES));

        //play Heart of Terra
        GameUtils.playCard(users.get(I_ACHILLES), lobbyId, 1087,
                privateGameWebSocketList.get(I_ACHILLES));

        //play attack on Kratos
        GameUtils.playCardOnTarget(users.get(I_ACHILLES), lobbyId, 1036,users.get(I_KRATOS).username(),
                privateGameWebSocketList.get(I_ACHILLES));

        //play heal as defend from kratos
        GameUtils.playCard(users.get(I_KRATOS), lobbyId, 1061,
                privateGameWebSocketList.get(I_KRATOS));

        //end turn
        GameUtils.endTurn(users.get(I_ACHILLES), lobbyId, false, true, false, privateGameWebSocketList.get(I_ACHILLES));
    }

    private void round3Hestia(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_HESTIA  );

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play equip Spear of Mars
        GameUtils.playCard(users.get(I_HESTIA), lobbyId, 1080,
                privateGameWebSocketList.get(I_HESTIA));

        //play Vulcanic eruption
        GameUtils.playCard(users.get(I_HESTIA), lobbyId, 1075,
                privateGameWebSocketList.get(I_HESTIA));

        //skip from Terpisore
        GameUtils.confirm(users.get(I_TERPISORE), lobbyId,
                privateGameWebSocketList.get(I_TERPISORE));

        //skip from Kratos
        GameUtils.confirm(users.get(I_KRATOS), lobbyId,
                privateGameWebSocketList.get(I_KRATOS));

        //skip from Achilles
        GameUtils.confirm(users.get(I_ACHILLES), lobbyId,
                privateGameWebSocketList.get(I_ACHILLES));

        //skill synergy order
        UseSkillRequest request = new UseSkillRequest(lobbyId, 0, List.of());
        GameUtils.useSkill(users.get(I_HESTIA), request, false, privateGameWebSocketList.get(I_HESTIA));


        //play cards which are used to swap new cards because of skill
        GameUtils.playMultipleCards(users.get(I_HESTIA), lobbyId, List.of(1031,1041),
                privateGameWebSocketList.get(I_HESTIA));

        //play Heal
        GameUtils.playCard(users.get(I_HESTIA), lobbyId, 1067,
                privateGameWebSocketList.get(I_HESTIA));

        //play attack on Kratos
        GameUtils.playCardOnTarget(users.get(I_HESTIA), lobbyId, 1015,users.get(I_KRATOS).username(),
                privateGameWebSocketList.get(I_HESTIA));

        //skip from Kratos
        GameUtils.confirm(users.get(I_KRATOS), lobbyId,
                privateGameWebSocketList.get(I_KRATOS));

        //end turn
        GameUtils.endTurn(users.get(I_HESTIA), lobbyId, false, true, false, privateGameWebSocketList.get(I_HESTIA));
    }

    private void round4Terpisore(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_TERPISORE);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play Attack on Achilles
        GameUtils.playCardOnTarget(users.get(I_TERPISORE), lobbyId, 1016, users.get(I_ACHILLES).username(),
                privateGameWebSocketList.get(I_TERPISORE));

        //skip from Achilles
        GameUtils.confirm(users.get(I_ACHILLES), lobbyId,
                privateGameWebSocketList.get(I_ACHILLES));

        //skill revenge von Achilles aktiviert
        UseSkillRequest request = new UseSkillRequest(lobbyId, 103, List.of());
        GameUtils.useSkill(users.get(I_ACHILLES), request, false, privateGameWebSocketList.get(I_ACHILLES));

        //play heal
        GameUtils.playCard(users.get(I_TERPISORE), lobbyId, 1063,
                privateGameWebSocketList.get(I_TERPISORE));

        //end turn
        GameUtils.endTurn(users.get(I_TERPISORE), lobbyId, false, true, true, privateGameWebSocketList.get(I_TERPISORE));
        GameUtils.discardCard(users.get(I_TERPISORE),lobbyId,List.of(1066),privateGameWebSocketList.get(I_TERPISORE));
    }


    private void round5Kratos(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_KRATOS);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play Godarena
        GameUtils.playCard(users.get(I_KRATOS), lobbyId, 1003,
                privateGameWebSocketList.get(I_KRATOS));

        //play attack from Achilles
        GameUtils.playCard(users.get(I_ACHILLES), lobbyId, 1026,
                privateGameWebSocketList.get(I_ACHILLES));

        //skip from Hestia
        GameUtils.confirm(users.get(I_HESTIA), lobbyId,
                privateGameWebSocketList.get(I_HESTIA));

        //play attack from terpisore
        GameUtils.playCard(users.get(I_TERPISORE), lobbyId, 1029,
                privateGameWebSocketList.get(I_TERPISORE));

        //end turn
        GameUtils.endTurn(users.get(I_KRATOS), lobbyId, false, true, false, privateGameWebSocketList.get(I_KRATOS));
    }

    private void round6Achilles(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_ACHILLES);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play heal
        GameUtils.playCard(users.get(I_ACHILLES), lobbyId, 1062,
                privateGameWebSocketList.get(I_ACHILLES));

        //play equip Spear of Ares
        GameUtils.playCard(users.get(I_ACHILLES), lobbyId, 1083,
                privateGameWebSocketList.get(I_ACHILLES));

        //attack on multiple target because of Spear of Ares
        GameUtils.playCardOnTargetList(users.get(I_ACHILLES), lobbyId,1013,List.of(users.get(I_KRATOS).username(),
                        users.get(I_TERPISORE).username(),users.get(I_HESTIA).username()),
                privateGameWebSocketList.get(I_ACHILLES));

        //skip from Kratos
        GameUtils.confirm(users.get(I_KRATOS), lobbyId,
                privateGameWebSocketList.get(I_KRATOS));

        //skip from Terpisore
        GameUtils.confirm(users.get(I_TERPISORE), lobbyId,
                privateGameWebSocketList.get(I_TERPISORE));
        //skip from Hestia
        GameUtils.confirm(users.get(I_HESTIA), lobbyId,
                privateGameWebSocketList.get(I_HESTIA));

        //end turn
        GameUtils.endTurn(users.get(I_ACHILLES), lobbyId, false, true, false, privateGameWebSocketList.get(I_ACHILLES));
    }

    private void round7Hestia(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_HESTIA);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //end turn
        GameUtils.endTurn(users.get(I_HESTIA), lobbyId, false, false, false, privateGameWebSocketList.get(I_HESTIA));
    }

    private void round8Terpisore(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_TERPISORE);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play heal
        GameUtils.playCard(users.get(I_TERPISORE), lobbyId, 1065,
                privateGameWebSocketList.get(I_TERPISORE));

        //play Attack on Achilles
        GameUtils.playCardOnTarget(users.get(I_TERPISORE), lobbyId, 1034, users.get(I_ACHILLES).username(),
                privateGameWebSocketList.get(I_TERPISORE));

        //skip from achilles
        GameUtils.confirm(users.get(I_ACHILLES), lobbyId,
                privateGameWebSocketList.get(I_ACHILLES));

        //skill synergy order
        UseSkillRequest request = new UseSkillRequest(lobbyId, 103, List.of());
        GameUtils.useSkill(users.get(I_ACHILLES), request, false, privateGameWebSocketList.get(I_ACHILLES));



        //end turn
        GameUtils.endTurn(users.get(I_TERPISORE), lobbyId, false, true, false, privateGameWebSocketList.get(I_TERPISORE));
    }

    private void round9Kratos(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_KRATOS);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play Nightmare on Achilles
        GameUtils.playCardOnTarget(users.get(I_KRATOS), lobbyId, 1071, users.get(I_ACHILLES).username(),
                privateGameWebSocketList.get(I_KRATOS));

        //end turn
        GameUtils.endTurn(users.get(I_KRATOS), lobbyId, false, false, true, privateGameWebSocketList.get(I_KRATOS));
        GameUtils.discardCard(users.get(I_KRATOS),lobbyId,List.of(1082),privateGameWebSocketList.get(I_KRATOS));

    }


    private void round11Hestia(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_HESTIA);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //end turn
        GameUtils.endTurn(users.get(I_HESTIA), lobbyId, false, false, true, privateGameWebSocketList.get(I_HESTIA));
        GameUtils.discardCard(users.get(I_HESTIA),lobbyId,List.of(1043,1022),privateGameWebSocketList.get(I_HESTIA));

    }

    private void round12Terpisore(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_TERPISORE);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //end turn
        GameUtils.endTurn(users.get(I_TERPISORE), lobbyId, false, false, false, privateGameWebSocketList.get(I_TERPISORE));

    }

    private void round13Kratos(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_KRATOS);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play vulcanic eruption
        GameUtils.playCard(users.get(I_KRATOS), lobbyId, 1076,
                privateGameWebSocketList.get(I_KRATOS));

        //skip from achilles
        GameUtils.confirm(users.get(I_ACHILLES), lobbyId,
                privateGameWebSocketList.get(I_ACHILLES));
        //bestätigen damit er nicht seinen skill revenge ausführt
        GameUtils.confirm(users.get(I_ACHILLES), lobbyId,
                privateGameWebSocketList.get(I_ACHILLES));

        //skip from Hestia
        GameUtils.confirm(users.get(I_HESTIA), lobbyId,
                privateGameWebSocketList.get(I_HESTIA));

        //skip from Terpisore
        GameUtils.confirm(users.get(I_TERPISORE), lobbyId,
                privateGameWebSocketList.get(I_TERPISORE));

        //end turn
        GameUtils.endTurn(users.get(I_KRATOS), lobbyId, false, true, true, privateGameWebSocketList.get(I_KRATOS));
        GameUtils.discardCard(users.get(I_KRATOS),lobbyId,List.of(1081),privateGameWebSocketList.get(I_KRATOS));

    }

    private void round14Achilles(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_ACHILLES);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //end turn
        GameUtils.endTurn(users.get(I_ACHILLES), lobbyId, false, false, true, privateGameWebSocketList.get(I_ACHILLES));
        GameUtils.discardCard(users.get(I_ACHILLES),lobbyId,List.of(1004,1011),privateGameWebSocketList.get(I_ACHILLES));
    }

    private void round15Hestia(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_HESTIA);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //end turn
        GameUtils.endTurn(users.get(I_HESTIA), lobbyId, false, false, true, privateGameWebSocketList.get(I_HESTIA));
        GameUtils.discardCard(users.get(I_HESTIA),lobbyId,List.of(1044,1074,1010),privateGameWebSocketList.get(I_HESTIA));
    }

    private void round16Terpisore(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_TERPISORE);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play eqip Heart of Terra
        GameUtils.playCard(users.get(I_TERPISORE), lobbyId, 1086,
                privateGameWebSocketList.get(I_TERPISORE));


        //end turn
        GameUtils.endTurn(users.get(I_TERPISORE), lobbyId, false, false, true, privateGameWebSocketList.get(I_TERPISORE));
        GameUtils.discardCard(users.get(I_TERPISORE),lobbyId,List.of(1048,1007),privateGameWebSocketList.get(I_TERPISORE));
    }

    private void round17Kratos(int lobbyId, List<StompFrameHandlerGame> privateGameWebSocketList) {
        StompFrameHandlerGame privateGameWebSocket = privateGameWebSocketList.get(I_KRATOS);

        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> assertTrue(privateGameWebSocket.containsType(GameMessageType.HIGHLIGHT)));

        //play eqip Heart of Terra
        GameUtils.playCardOnTarget(users.get(I_KRATOS), lobbyId, 1057, users.get(I_HESTIA).username(),
                privateGameWebSocketList.get(I_KRATOS));



        //end turn
        GameUtils.endTurn(users.get(I_TERPISORE), lobbyId, false, false, true, privateGameWebSocketList.get(I_TERPISORE));
        GameUtils.discardCard(users.get(I_TERPISORE),lobbyId,List.of(1048,1007),privateGameWebSocketList.get(I_TERPISORE));
    }
}

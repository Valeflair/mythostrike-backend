package com.mythostrike.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mythostrike.controller.message.game.CardMoveMessage;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.HandCards;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.Lobby;
import com.mythostrike.model.lobby.LobbyList;
import com.mythostrike.model.lobby.LobbyStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

@Slf4j
@RequiredArgsConstructor
@Service
public final class WebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final GameController gameController;

    private final LobbyController lobbyController;

    private final LobbyList lobbyList = LobbyList.getLobbyList();

    public void sendMessage(String path, Object payload, String messageName) {

        log.debug("{} to '{}'", messageName, path);
        simpMessagingTemplate.convertAndSend(path, payload);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(payload);
            log.debug("sent to frontend: {}", json);
        } catch (JsonProcessingException e) {
            log.error("could not convert {} to json {}", messageName, e);
        }
    }

    @EventListener
    /**
     * If a client subscribes to a lobby, all players get an update lobby message.
     * If a client subscribes to a game, all players get an update game message.
     *   The connected players are increased.
     *   If all players are connected after the championselection, the game starts.
     * If a client subscribes to a game private, the player gets his current handcards send.
     */
    private void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        //get path where client subscribed
        String path = (String) event.getMessage().getHeaders().get("simpDestination");
        if (path == null) return;
        log.debug("client subscribed to '{}'", path);


        //check if path matches /lobbies/{lobbyId} and extract lobbyId
        Pattern lobbyPattern = Pattern.compile("/lobbies/(\\d+)");
        Matcher lobbyMatcher = lobbyPattern.matcher(path);

        //or if it matches /games/{lobbyId} and extract lobbyId
        Pattern gamePattern = Pattern.compile("/games/(\\d+)");
        Matcher gameMatcher = gamePattern.matcher(path);
        //or if it matches /lobbies/{lobbyId}/{username} and extract lobbyId and username
        Pattern gamePrivatePattern = Pattern.compile("/games/(\\d+)/(.+)");
        Matcher gamePrivateMatcher = gamePrivatePattern.matcher(path);

        if (lobbyMatcher.matches()) {
            //send update Lobby message to client
            int lobbyId = Integer.parseInt(lobbyMatcher.group(1));
            lobbyController.updateLobby(lobbyId);
        } else if (gameMatcher.matches()) {
            //send update Game message to client and increase the number of connected players
            int lobbyId = Integer.parseInt(lobbyMatcher.group(1));
            updateGame(lobbyId);
        } else if (gamePrivateMatcher.matches()) {
            //send current Handcards to client
            int lobbyId = Integer.parseInt(lobbyMatcher.group(1));
            String username = gamePrivateMatcher.group(2);
            sendCurrentHandcards(lobbyId, username);
        }
    }

    /**
     * Send an update game message to client and increase the number of connected players.
     * If all players are connected after the championselection, the game starts.
     * @param lobbyId id of the lobby
     */
    private void updateGame(int lobbyId) {
        //TODO: in thread
        //shortly wait, sometimes the client is not ready to receive the game update
        try {
            sleep(100);
        } catch (InterruptedException e) {
            log.error("could not sleep", e);
        }
        //send an game update to client
        gameController.updateGame(lobbyId);
        lobbyList.increaseUserInGame(lobbyId);

        //when all players are connected start the normal game procedure
        if (lobbyList.isGameReadyToStart(lobbyId)) {
            lobbyList.setLobbyGameRunning(lobbyId);
            log.debug("all players are connected");
            GameManager gameManager = lobbyList.getGameManager(lobbyId);
            if (gameManager == null) {
                log.error("gameManager is null, but enough players are in game");
                return;
            }
            gameManager.allPlayersConnected();
            gameController.updateGame(lobbyId);
        }
    }

    /**
     * Send the client his current handcards.
     * @param lobbyId id of the lobby
     * @param username username of the client
     */
    private void sendCurrentHandcards(int lobbyId, String username) {
        //TODO: in thread
        //shortly wait, sometimes the client is not ready to receive the game update
        try {
            sleep(100);
        } catch (InterruptedException e) {
            log.error("could not sleep", e);
        }
        GameManager gameManager = lobbyList.getGameManager(lobbyId);
        if (gameManager == null) throw new IllegalStateException("gameManager is null, but game is running");

        HandCards handCards = gameManager.getPlayerByName(username).getHandCards();

        CardMoveMessage cardMoveMessage = new CardMoveMessage(
            username, username, handCards.size(),
            GameManager.convertCardsToInteger(handCards.getCards())
        );

        gameController.cardMove(lobbyId, List.of(username), cardMoveMessage);
    }

    @EventListener
    /**
     * If a client unsubscribes from a lobby, the client is removed from the lobby.
     * If a client unsubscribes from a game, the client still stays in the game and can reconnect.
     *   While he is disconnected, the game will continue without him. His rounds will be terminated with the timebar.
     *   The number of connected players is counted down. If no players are connected, the game will be terminated.
     */
    private void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        //get path where client subscribed
        String path = (String) event.getMessage().getHeaders().get("simpDestination");
        if (path == null) return;
        log.debug("client subscribed to '{}'", path);

        //check if path matches /lobbies/{lobbyId}/{username} and extract lobbyId and username
        Pattern lobbyPrivatePattern = Pattern.compile("/lobbies/(\\d+)/(.+)");
        Matcher lobbyPrivateMatcher = lobbyPrivatePattern.matcher(path);
        //or if it matches /games/{lobbyId} and extract lobbyId
        Pattern gamePattern = Pattern.compile("/games/(\\d+)");
        Matcher gameMatcher = gamePattern.matcher(path);


        if (lobbyPrivateMatcher.matches()) {
            //remove player from lobby
            int lobbyId = Integer.parseInt(lobbyPrivateMatcher.group(1));
            String username = lobbyPrivateMatcher.group(2);
            Lobby lobby = lobbyList.getLobby(lobbyId);
            if (lobby == null) throw new IllegalInputException("lobby does not exist, but client was subscribed to it");
            lobby.removeUser(username);
        } else if (gameMatcher.matches()) {
            //decrease number of connected players
            int lobbyId = Integer.parseInt(gameMatcher.group(1));
            lobbyList.decreaseUserInGame(lobbyId);
        }
    }
}

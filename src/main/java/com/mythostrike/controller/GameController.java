package com.mythostrike.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mythostrike.controller.message.game.CardMoveMessage;
import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.LogMessage;
import com.mythostrike.controller.message.game.PlayerData;
import com.mythostrike.controller.message.game.PlayerResult;
import com.mythostrike.controller.message.game.SelectCardsRequest;
import com.mythostrike.controller.message.game.SelectChampionRequest;
import com.mythostrike.controller.message.game.UseSkillRequest;
import com.mythostrike.controller.message.game.GameMessage;
import com.mythostrike.controller.message.game.GameMessageType;
import com.mythostrike.controller.message.lobby.LobbyIdRequest;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardList;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Champion;
import com.mythostrike.model.game.player.ChampionList;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.LobbyList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController("gameController")
@RequestMapping("/games/play")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final LobbyList lobbyList = LobbyList.getLobbyList();

    private final ChampionList championList = ChampionList.getChampionList();

    private final CardList cardList = CardList.getCardList();

    private final WebSocketService webSocketService;

    @PostMapping("/champion")
    public ResponseEntity<Void> selectChampion(Principal principal, @RequestBody SelectChampionRequest request)
        throws IllegalInputException {
        log.debug("select champion '{}' request in '{}' from '{}'", request.championId(), request.lobbyId(),
            principal.getName());

        //get objects from REST data
        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        Champion champion = championList.getChampion(request.championId());

        gameManager.selectChampion(principal.getName(), champion);

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/cards")
    public ResponseEntity<Void> selectCards(Principal principal, @RequestBody SelectCardsRequest request)
        throws IllegalInputException {
        List<String> cardNames = new ArrayList<>();
        if (request.cardIds() != null) {
            cardNames = request.cardIds().stream().map(cardList::getCard).map(Card::toString).toList();
        }

        log.debug("play card '{}' request in '{}' from '{}' to targets '{}'", cardNames, request.lobbyId(),
            principal.getName(), request.targets());

        //get objects from REST data
        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        gameManager.selectCards(principal.getName(), request.cardIds(), request.targets());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/skills")
    public ResponseEntity<Void> useSkill(Principal principal, @RequestBody UseSkillRequest request) {
        log.debug("use skill '{}' request in '{}' from '{}' to targets '{}'", request.skillId(), request.lobbyId(),
            principal.getName(), request.targets());

        //get objects from REST data
        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        gameManager.selectSkill(principal.getName(), request.skillId(), request.targets());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/end")
    public ResponseEntity<Void> endTurn(Principal principal, @RequestBody LobbyIdRequest request) {
        log.debug("end turn request in '{}' from '{}'", request.lobbyId(), principal.getName());

        //get objects from REST data
        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        gameManager.endTurn(principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    public void selectChampionFrom(int lobbyId, String toUsername, ChampionSelectionMessage message) {
        String path = String.format("/lobbies/%d/%s", lobbyId, toUsername);

        webSocketService.sendMessage(path, message, "selectChampionFrom");
    }

    public void updateGame(int lobbyId) {
        GameManager gameManager = lobbyList.getGameManager(lobbyId);
        //game could have ended, so check if it still exists
        if (gameManager == null) {
            log.debug("Game {} not found, can't update", lobbyId);
            return;
        }

        List<Player> players = gameManager.getGame().getAllPlayers().stream().filter(Objects::nonNull).toList();
        List<PlayerData> playerDatas = new ArrayList<>();
        boolean isCurrentPlayer;

        for (Player player : players) {
            isCurrentPlayer = player.equals(gameManager.getGame().getCurrentPlayer());
            playerDatas.add(new PlayerData(player, isCurrentPlayer));
        }

        updateGame(lobbyId, playerDatas);
    }

    private void updateGame(int lobbyId, List<PlayerData> playerDatas) {
        String path = String.format("/games/%d", lobbyId);

        webSocketService.sendMessage(path, new GameMessage(GameMessageType.UPDATE_GAME, playerDatas),
            "updateGame");
    }

    public void highlight(int lobbyId, String toUsername, HighlightMessage message) {
        String path = String.format("/games/%d/%s", lobbyId, toUsername);

        webSocketService.sendMessage(path, new GameMessage(GameMessageType.HIGHLIGHT, message),
            "highlight");
    }

    /**
     * send a card move message to all players in the game
     *
     * @param lobbyId id of the game
     * @param message the card move message to send
     */
    public void cardMove(int lobbyId, CardMoveMessage message) {
        String path = String.format("/games/%d", lobbyId);

        //get all usernames to send to
        GameManager gameManager = lobbyList.getGameManager(lobbyId);
        if (gameManager == null) {
            log.debug("Game {} not found, can't update", lobbyId);
            return;
        }
        List<String> toUsernames = gameManager.getGame().getAllPlayers().stream().filter(Objects::nonNull)
            .map(Player::getUsername).toList();

        //make only one log message
        GameMessage payload = new GameMessage(GameMessageType.CARD_MOVE, message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(payload);
            log.debug("public card move to '{}': {}", path, json);
        } catch (JsonProcessingException e) {
            log.error("could not convert public card move to json {}", e.toString());
        }

        //send message to all players without logging
        for (String username : toUsernames) {
            path = String.format("/games/%d/%s", lobbyId, username);
            webSocketService.sendMessageWithoutLog(path, payload);
        }
    }

    /**
     * send a card move message to the specified players
     * log for each player separately
     *
     * @param lobbyId     id of the game
     * @param toUsernames usernames of the players to send to
     * @param message     the card move message to send
     */
    public void cardMove(int lobbyId, List<String> toUsernames, CardMoveMessage message) {
        for (String username : toUsernames) {
            String path = String.format("/games/%d/%s", lobbyId, username);

            webSocketService.sendMessage(path, new GameMessage(GameMessageType.CARD_MOVE, message),
                "cardMovePrivate");
        }
    }

    public void logMessage(int lobbyId, String message) {
        String path = String.format("/games/%d", lobbyId);

        webSocketService.sendMessage(path,
            new GameMessage(GameMessageType.LOG, new LogMessage(message)), "logMessage");
    }

    public void gameEnd(int lobbyId, List<PlayerResult> results) {
        String path = String.format("/games/%d", lobbyId);

        //remove game from lobby list
        lobbyList.removeLobby(lobbyId);

        webSocketService.sendMessage(path, new GameMessage(GameMessageType.GAME_END, results),
            "gameEnd");
    }
}

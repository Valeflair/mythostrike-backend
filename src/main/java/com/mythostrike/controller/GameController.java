package com.mythostrike.controller;

import com.mythostrike.controller.message.game.CardMoveMessage;
import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.LogMessage;
import com.mythostrike.controller.message.game.PlayerData;
import com.mythostrike.controller.message.game.PlayerResult;
import com.mythostrike.controller.message.game.SelectCardsRequest;
import com.mythostrike.controller.message.game.SelectChampionRequest;
import com.mythostrike.controller.message.game.SelectTargetRequest;
import com.mythostrike.controller.message.game.UseSkillRequest;
import com.mythostrike.controller.message.game.WebSocketGameMessage;
import com.mythostrike.controller.message.game.WebSocketGameMessageType;
import com.mythostrike.controller.message.lobby.LobbyIdRequest;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardList;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Champion;
import com.mythostrike.model.game.player.ChampionList;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.LobbyList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        log.debug("play card '{}' request in '{}' from '{}'", request.cardIds(), request.lobbyId(),
            principal.getName());

        //get objects from REST data
        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        gameManager.selectCards(principal.getName(), request.cardIds());

        updateGame(request.lobbyId());
        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelRequest(Principal principal, @RequestBody LobbyIdRequest request) {
        log.debug("cancel request in '{}' from '{}'", request.lobbyId(), principal.getName());

        //get objects from REST data
        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        gameManager.cancelRequest(principal.getName());

        updateGame(request.lobbyId());
        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/targets")
    public ResponseEntity<Void> selectTargets(Principal principal, @RequestBody SelectTargetRequest request) {
        log.debug("select '{}' as targets request in '{}' from '{}'", request.targets(), request.lobbyId(),
            principal.getName());

        //get objects from REST data
        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        gameManager.selectPlayers(principal.getName(), request.targets());

        updateGame(request.lobbyId());
        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/skills")
    public ResponseEntity<Void> useSkill(Principal principal, @RequestBody UseSkillRequest request) {
        log.debug("use skill '{}' request in '{}' from '{}'", request.skillId(), request.lobbyId(),
            principal.getName());

        //get objects from REST data
        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        gameManager.selectSkill(principal.getName(), request.skillId());


        updateGame(request.lobbyId());
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

        updateGame(request.lobbyId());
        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    public void selectChampionFrom(int lobbyId, String toUsername, ChampionSelectionMessage message) {
        String path = String.format("/lobbies/%d/%s", lobbyId, toUsername);

        webSocketService.sendMessage(path, message, "selectChampionFrom");
    }

    public void updateGame(int lobbyId) {
        GameManager gameManager = lobbyList.getGameManager(lobbyId);
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
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

    public void updateGame(int lobbyId, List<PlayerData> playerDatas) {
        String path = String.format("/games/%d", lobbyId);

        webSocketService.sendMessage(path, new WebSocketGameMessage(WebSocketGameMessageType.UPDATE_GAME, playerDatas),
            "updateGame");
    }

    public void highlight(int lobbyId, String toUsername, HighlightMessage message) {
        String path = String.format("/games/%d/%s", lobbyId, toUsername);

        webSocketService.sendMessage(path, new WebSocketGameMessage(WebSocketGameMessageType.HIGHLIGHT, message),
            "highlight");
    }

    public void cardMove(int lobbyId, CardMoveMessage message) {
        String path = String.format("/games/%d", lobbyId);

        webSocketService.sendMessage(path, new WebSocketGameMessage(WebSocketGameMessageType.CARD_MOVE, message),
            "cardMove");
    }

    public void cardMove(int lobbyId, List<String> toUsernames, CardMoveMessage message) {
        for (String username : toUsernames) {
            String path = String.format("/games/%d/%s", lobbyId, username);

            webSocketService.sendMessage(path, new WebSocketGameMessage(WebSocketGameMessageType.CARD_MOVE, message),
                "cardMovePrivate");
        }
    }

    public void logMessage(int lobbyId, String message) {
        String path = String.format("/games/%d", lobbyId);

        webSocketService.sendMessage(path,
            new WebSocketGameMessage(WebSocketGameMessageType.LOG, new LogMessage(message)), "logMessage");
    }

    public void gameEnd(int lobbyId, List<PlayerResult> results) {
        String path = String.format("/games/%d", lobbyId);

        webSocketService.sendMessage(path, new WebSocketGameMessage(WebSocketGameMessageType.GAME_END, results),
            "gameEnd");
    }

    @EventListener
    private void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        //get path where client subscribed
        String path = (String) event.getMessage().getHeaders().get("simpDestination");
        if (path == null) return;
        log.debug("client subscribed to '{}'", path);

        //check if path matches /games/{lobbyId} and extract lobbyId
        Pattern pattern = Pattern.compile("/games/(\\d+)");
        Matcher matcher = pattern.matcher(path);
        if (!matcher.matches()) return;
        String idAsString = matcher.group(1);
        int lobbyId = Integer.parseInt(idAsString);

        //send an game update to client
        updateGame(lobbyId);
        lobbyList.increaseUserInGame(lobbyId);

        //when all players are connected start the normal game procedure
        if (lobbyList.isInGameFull(lobbyId)) {
            log.debug("all players are connected");
            GameManager gameManager = lobbyList.getGameManager(lobbyId);
            if (gameManager == null) {
                log.error("gameManager is null, but enough players are in game");
                return;
            }
            gameManager.allPlayersConnected();
            updateGame(lobbyId);
        }
    }
}

package com.mythostrike.controller;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.game.CardMoveMessage;
import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.controller.message.game.DiscardCardRequest;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerData;
import com.mythostrike.controller.message.game.SelectCardRequest;
import com.mythostrike.controller.message.game.SelectChampionRequest;
import com.mythostrike.controller.message.game.UseCardRequest;
import com.mythostrike.controller.message.game.UseSkillRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games/play")
@Slf4j
public class GameController {

    private final UserService userService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final LobbyList lobbyList = LobbyList.getLobbyList();

    private final ChampionList championList = ChampionList.getChampionList();

    private final CardList cardList = CardList.getCardList();

    @PostMapping("/champion")
    public ResponseEntity<Void> selectChampion(Principal principal, @RequestBody SelectChampionRequest request)
        throws IllegalInputException {
        log.debug("select champion '{}' request in '{}' from '{}'", request.championId(), request.lobbyId(),
            principal.getName());

        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        Champion champion = championList.getChampion(request.championId());

        //gameManager.selectChampion(principal.getName(), champion);

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/cards")
    public ResponseEntity<Void> selectCardToPlay(Principal principal, @RequestBody SelectCardRequest request)
        throws IllegalInputException {
        log.debug("play card '{}' request in '{}' from '{}'", request.cardId(), request.lobbyId(), principal.getName());

        GameManager gameManager = lobbyList.getGameManager(request.lobbyId());
        if (gameManager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        Card card = cardList.getCard(request.cardId());
        //gameManager.selectCard(principal.getName(), card);


        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelSelection(Principal principal, @RequestBody LobbyIdRequest request) {
        log.debug("cancel request in '{}' from '{}'", request.lobbyId(), principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/targets")
    public ResponseEntity<Void> useCard(Principal principal, @RequestBody UseCardRequest request) {
        log.debug("use card '{}' on '{}' request in '{}' from '{}'", request.cardId(), request.targets(),
            request.lobbyId(), principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/discard")
    public ResponseEntity<Void> discardCard(Principal principal, @RequestBody DiscardCardRequest request) {
        log.debug("discard cards '{}' request in '{}' from '{}'", request.cardIdList(), request.lobbyId(),
            principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/skills")
    public ResponseEntity<Void> useSkill(Principal principal, @RequestBody UseSkillRequest request) {
        log.debug("use skill '{}' request in '{}' from '{}'", request.skillId(), request.lobbyId(),
            principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/end")
    public ResponseEntity<Void> endTurn(Principal principal, @RequestBody LobbyIdRequest request) {
        log.debug("end turn request in '{}' from '{}'", request.lobbyId(), principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    public void selectChampionFrom(int lobbyId, String toUsername, ChampionSelectionMessage message) {

    }

    public void highlight(int lobbyId, String toUsername, HighlightMessage message) {

    }

    public void updateGame(int lobbyId, String logMessage, List<PlayerData> playerDatas) {

    }

    public void cardMove(int lobbyId, CardMoveMessage message) {

    }

    public void cardMove(int lobbyId, List<String> toUsernames, CardMoveMessage message) {

    }
}

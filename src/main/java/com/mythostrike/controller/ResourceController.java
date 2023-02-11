package com.mythostrike.controller;

import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.controller.message.lobby.LobbyOverview;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardList;
import com.mythostrike.model.game.player.Champion;
import com.mythostrike.model.game.player.ChampionList;
import com.mythostrike.model.lobby.Mode;
import com.mythostrike.model.lobby.ModeList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
@Slf4j
public class ResourceController {

    private final ModeList modeList = ModeList.getModeList();

    private final ChampionList championList = ChampionList.getChampionList();

    private final CardList cardList = CardList.getCardList();

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getCards() {
        log.debug("getCards request");
        return ResponseEntity.ok(cardList.getCards());
    }

    @GetMapping("/modes")
    public ResponseEntity<List<Mode>> getModes() {
        log.debug("getModes request");
        return ResponseEntity.ok(modeList.getModes());
    }

    @GetMapping("/champions")
    public ResponseEntity<List<Champion>> getChampions() {
        log.debug("getChampions request");
        return ResponseEntity.ok(championList.getChampions());
    }
}

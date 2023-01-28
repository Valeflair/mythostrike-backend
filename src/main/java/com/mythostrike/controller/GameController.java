package com.mythostrike.controller;

import com.mythostrike.controller.message.game.*;
import com.mythostrike.controller.message.lobby.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games/play")
@Slf4j
public class GameController {

    @PostMapping("/champion")
    public ResponseEntity<Void> selectChampion(Principal principal, @RequestBody SelectChampionRequest request) {
        log.debug("select champion '{}' request in '{}' from '{}'", request.championId(), request.lobbyId(),
                principal.getName());

        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/cards")
    public ResponseEntity<Void> selectCard(Principal principal, @RequestBody SelectCardRequest request) {
        log.debug("play card '{}' request in '{}' from '{}'", request.cardId(), request.lobbyId(), principal.getName());

        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    //TODO: openApi noch anpassen
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
}


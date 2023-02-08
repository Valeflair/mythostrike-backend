package com.mythostrike.controller;

import com.mythostrike.controller.message.lobby.LobbyOverview;
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

    @GetMapping("/cards")
    public ResponseEntity<Void> getCards() {
        log.debug("getCards request");
        //TODO:
        //return ResponseEntity.ok(list);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/modes")
    public ResponseEntity<List<Mode>> getModes() {
        log.debug("getModes request");
        return ResponseEntity.ok(modeList.getModes());
    }

    @GetMapping("/champions")
    public ResponseEntity<List<LobbyOverview>> getChampions() {
        log.debug("getChampions request");
        //return ResponseEntity.ok(list);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

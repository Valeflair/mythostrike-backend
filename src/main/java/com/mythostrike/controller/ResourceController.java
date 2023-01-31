package com.mythostrike.controller;

import com.mythostrike.model.lobby.Mode;
import com.mythostrike.model.lobby.ModeList;
import com.mythostrike.model.lobby.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
@Slf4j
public class ResourceController {

    @GetMapping("/cards")
    public ResponseEntity<Map<String, Seat>> getCards() {
        log.debug("getCards request");
        //TODO:
        //return ResponseEntity.ok(list);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/modes")
    public ResponseEntity<List<Mode>> getModes() {
        log.debug("getModes request");
        return ResponseEntity.ok(ModeList.getModeList().getAllModes());
    }

    @GetMapping("/champions")
    public ResponseEntity<Void> getChampions() {
        log.debug("getChampions request");
        //TODO:
        //return ResponseEntity.ok(list);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

package com.mythostrike.controller;

import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.lobby.*;
import com.mythostrike.model.lobby.Lobby;
import com.mythostrike.model.lobby.LobbyList;
import com.mythostrike.model.lobby.ModeList;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lobbies")
@Slf4j
public class LobbyController {

    //TODO implementieren
    //private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserService userService;

    private final LobbyList lobbyList = LobbyList.getLobbyList();

    private final ModeList modeList = ModeList.getModeList();

    @GetMapping
    public ResponseEntity<List<LobbyOverview>> getLobbies() {
        log.debug("getLobbies request");
        //TODO: test erstellen
        /*List<LobbyOverview> list = new ArrayList<>();
        list.add(new LobbyOverview(2, "Hyuman123", "active", "FFA", 5));
        list.add(new LobbyOverview(3, "Hyuman123", "active", "1 vs. 1", 5));
        list.add(new LobbyOverview(4, "Till", "active", "4 vs. 4", 5));*/
        return ResponseEntity.ok(lobbyList.getLobbiesOverview());
    }

    @PostMapping
    public ResponseEntity<LobbyIdRequest> create(Principal principal, @RequestBody CreateLobbyRequest request) {
        log.debug("create lobby request from '{}' with  mode '{}", principal.getName(), request.modeId());

        //TODO: exceptions werfen
        //TODO: exception handling
        try {
            modeList.getMode(request.modeId());
        } catch (IllegalArgumentException e) {
            log.debug("mode '{}' not found", request.modeId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        try {
            userService.getUser(principal.getName());
        } catch (EntityNotFoundException e) {
            log.error("user not found: '{}'", principal.getName());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

        Lobby lobby = lobbyList.createLobby(userService.getUser(principal.getName()),
            modeList.getMode(request.modeId()));
        log.debug("created lobby '{}' with  mode '{}", lobby.getId(), request.modeId());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new LobbyIdRequest(lobby.getId()));
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(Principal principal, @RequestBody LobbyIdRequest request) {
        log.debug("join lobby '{}' request from '{}'", request.lobbyId(), principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leave(Principal principal, @RequestBody LobbyIdRequest request) {
        log.debug("leave lobby '{}' request from '{}'", request.lobbyId(), principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PutMapping("/mode")
    public ResponseEntity<Void> changeMode(Principal principal, @RequestBody ChangeModeRequest request) {
        log.debug("change mode in '{}' to '{}' request from '{}'", request.lobbyId(), request.newModeId(),
            principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PutMapping("/seats")
    public ResponseEntity<Void> changeSeat(Principal principal, @RequestBody ChangeSeatRequest request) {
        log.debug("cange seat in '{}' to '{}' request from '{}'", request.lobbyId(), request.newSeatId(),
            principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/bot")
    public ResponseEntity<Void> addBot(Principal principal, @RequestBody LobbyIdRequest request) {
        log.debug("add bot to '{}' request from '{}'", request.lobbyId(), principal.getName());

        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start(Principal principal, @RequestBody LobbyIdRequest request) {
        log.debug("start game '{}' request from '{}'", request.lobbyId(), principal.getName());

        return ResponseEntity
            .status(HttpStatus.CREATED).build();
    }

    /*public void updateLobby(int lobbyId) {
        simpMessagingTemplate.convertAndSend("/lobbies/" + lobbyId,
            lobbyList.getLobby(lobbyId));
    }*/
}

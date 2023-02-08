package com.mythostrike.controller;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.lobby.ChangeModeRequest;
import com.mythostrike.controller.message.lobby.ChangeSeatRequest;
import com.mythostrike.controller.message.lobby.CreateLobbyRequest;
import com.mythostrike.controller.message.lobby.LobbyIdRequest;
import com.mythostrike.controller.message.lobby.LobbyOverview;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.lobby.Lobby;
import com.mythostrike.model.lobby.LobbyList;
import com.mythostrike.model.lobby.Mode;
import com.mythostrike.model.lobby.ModeList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lobbies")
@Slf4j
public class LobbyController {

    private final UserService userService;

    private final SimpMessagingTemplate simpMessagingTemplate;

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
    public ResponseEntity<Lobby> create(Principal principal, @RequestBody CreateLobbyRequest request)
        throws IllegalInputException {
        log.debug("create lobby request from '{}' with  mode '{}", principal.getName(), request.modeId());

        //get objects from REST data
        User user = userService.getUser(principal.getName());
        Mode mode = modeList.getMode(request.modeId());

        //create lobby
        Lobby lobby = lobbyList.createLobby(user, mode);
        log.debug("created lobby '{}' with  mode '{}", lobby.getId(), request.modeId());

        updateLobby(lobby.getId());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(lobby);
    }

    @PostMapping("/join")
    public ResponseEntity<Lobby> join(Principal principal, @RequestBody LobbyIdRequest request)
        throws IllegalInputException {
        log.debug("join lobby '{}' request from '{}'", request.lobbyId(), principal.getName());

        //get objects from REST data
        User user = userService.getUser(principal.getName());
        Lobby lobby = lobbyList.getLobby(request.lobbyId());
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
        }

        //join user to lobby
        if (!lobby.addUser(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is full");
        }

        updateLobby(request.lobbyId());
        return ResponseEntity.ok(lobby);
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leave(Principal principal, @RequestBody LobbyIdRequest request)
        throws IllegalInputException {
        log.debug("leave lobby '{}' request from '{}'", request.lobbyId(), principal.getName());

        //get objects from REST data
        User user = userService.getUser(principal.getName());
        Lobby lobby = lobbyList.getLobby(request.lobbyId());
        if (lobby == null) {
            throw new IllegalInputException("Lobby not found");
        }

        //leave user from lobby
        if (!lobby.removeUser(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not in lobby");
        }

        //TODO: remove lobby if empty

        updateLobby(request.lobbyId());
        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PutMapping("/mode")
    public ResponseEntity<Void> changeMode(Principal principal, @RequestBody ChangeModeRequest request)
        throws IllegalInputException {
        log.debug("change mode in '{}' to '{}' request from '{}'", request.lobbyId(), request.newModeId(),
            principal.getName());

        //get objects from REST data
        User user = userService.getUser(principal.getName());
        Lobby lobby = lobbyList.getLobby(request.lobbyId());
        Mode mode = modeList.getMode(request.newModeId());
        if (lobby == null) {
            throw new IllegalInputException("Lobby not found");
        }

        //change mode in lobby
        lobby.changeMode(mode, user);

        updateLobby(request.lobbyId());
        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PutMapping("/seats")
    public ResponseEntity<Void> changeSeat(Principal principal, @RequestBody ChangeSeatRequest request)
        throws IllegalInputException {
        log.debug("change seat in '{}' to '{}' request from '{}'", request.lobbyId(), request.newSeatId(),
            principal.getName());

        //get objects from REST data
        User user = userService.getUser(principal.getName());
        Lobby lobby = lobbyList.getLobby(request.lobbyId());
        if (lobby == null) {
            throw new IllegalInputException("Lobby not found");
        }

        //change seat of the player
        if (!lobby.changeSeat(request.newSeatId(), user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is already taken");
        }

        updateLobby(request.lobbyId());
        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/bot")
    public ResponseEntity<Void> addBot(Principal principal, @RequestBody LobbyIdRequest request)
        throws IllegalInputException {
        log.debug("add bot to '{}' request from '{}'", request.lobbyId(), principal.getName());

        //get objects from REST data
        User user = userService.getUser(principal.getName());
        Lobby lobby = lobbyList.getLobby(request.lobbyId());
        if (lobby == null) {
            throw new IllegalInputException("Lobby not found");
        }

        //add bot to lobby
        if (!lobby.addBot(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is full");
        }

        updateLobby(request.lobbyId());
        return ResponseEntity
            .status(HttpStatus.OK).build();
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start(Principal principal, @RequestBody LobbyIdRequest request)
        throws IllegalInputException {
        log.debug("start game '{}' request from '{}'", request.lobbyId(), principal.getName());
        //get objects from REST data
        User user = userService.getUser(principal.getName());
        Lobby lobby = lobbyList.getLobby(request.lobbyId());
        if (lobby == null) {
            throw new IllegalInputException("Lobby not found");
        }

        //start game
        if (!lobby.createGame(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not the correct number of players");
        }

        return ResponseEntity
            .status(HttpStatus.CREATED).build();
    }

    //TODO: remove
    @PostMapping("/lobby")
    public ResponseEntity<Lobby> getLobby(Principal principal, @RequestBody LobbyIdRequest request)
        throws IllegalInputException {
        log.debug("get lobby '{}' request from '{}'", request.lobbyId(), principal.getName());
        //get objects from REST data
        Lobby lobby = lobbyList.getLobby(request.lobbyId());
        if (lobby == null) {
            throw new IllegalInputException("Lobby not found");
        }
        return ResponseEntity.ok(lobby);
    }

    private void updateLobby(int lobbyId) {
        Lobby lobby = lobbyList.getLobby(lobbyId);
        if (lobby == null) {
            return;
        }
        String path = "/lobbies/" + lobbyId;

        log.debug("update lobby on '{}'", path);
        simpMessagingTemplate.convertAndSend(path, lobby);
    }
}

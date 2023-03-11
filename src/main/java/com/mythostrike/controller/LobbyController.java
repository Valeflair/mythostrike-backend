package com.mythostrike.controller;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.lobby.ChangeModeRequest;
import com.mythostrike.controller.message.lobby.ChangeSeatRequest;
import com.mythostrike.controller.message.lobby.CreateLobbyRequest;
import com.mythostrike.controller.message.lobby.LobbyIdRequest;
import com.mythostrike.controller.message.lobby.LobbyMessage;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lobbies")
@Slf4j
public class LobbyController {

    public static final String LOBBY_NOT_FOUND_MESSAGE = "Lobby not found";

    private final LobbyList lobbyList = LobbyList.getLobbyList();

    private final ModeList modeList = ModeList.getModeList();

    private final WebSocketService webSocketService;

    //@Autowired didnt work in GameManager so i used @RequiredArgsConstructor and handed it through to the GameManager
    private final GameController gameController;

    //same goes for userService and Human. Sothat autwired works, the objekt hast to be a component, but we dont want
    //that there, because the object has a state.
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<LobbyOverview>> getLobbies() {
        log.debug("getLobbies request");
        //TODO: test erstellen
        /*List<LobbyOverview> list = new ArrayList<>();
        list.add(new LobbyOverview(2, "Hyuman123", "active", "FFA", 5));
        list.add(new LobbyOverview(3, "Hyuman123", "active", "1 vs. 1", 5));
        list.add(new LobbyOverview(4, "Till", "active", "4 vs. 4", 5));*/
        //sort the list by the id
        return ResponseEntity.ok(
            lobbyList.getLobbiesOverview().stream().sorted(Comparator.comparingInt(LobbyOverview::id)).toList()
        );
    }

    @PostMapping
    public ResponseEntity<LobbyMessage> create(Principal principal, @RequestBody CreateLobbyRequest request)
        throws IllegalInputException {
        log.debug("create lobby request from '{}' with  mode '{}", principal.getName(), request.modeId());

        //get objects from REST data
        User user = userService.getUser(principal.getName());
        Mode mode = modeList.getMode(request.modeId());

        //create lobby
        Lobby lobby = lobbyList.createLobby(user, mode, userService);
        log.debug("created lobby '{}' with  mode '{}", lobby.getId(), request.modeId());

        updateLobby(lobby.getId());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new LobbyMessage(lobby));
    }

    @PostMapping("/join")
    public ResponseEntity<LobbyMessage> join(Principal principal, @RequestBody LobbyIdRequest request)
        throws IllegalInputException {
        log.debug("join lobby '{}' request from '{}'", request.lobbyId(), principal.getName());

        //get objects from REST data
        User user = userService.getUser(principal.getName());
        Lobby lobby = lobbyList.getLobby(request.lobbyId());
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, LOBBY_NOT_FOUND_MESSAGE);
        }

        //join user to lobby
        if (!lobby.addUser(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is full");
        }

        updateLobby(request.lobbyId());
        return ResponseEntity.ok(new LobbyMessage(lobby));
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leave(Principal principal, @RequestBody LobbyIdRequest request)
        throws IllegalInputException {
        log.debug("leave lobby '{}' request from '{}'", request.lobbyId(), principal.getName());

        //get objects from REST data
        User user = userService.getUser(principal.getName());
        if (!lobbyList.removeUser(request.lobbyId(), user)) {
            updateLobby(request.lobbyId());
        }

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
            throw new IllegalInputException(LOBBY_NOT_FOUND_MESSAGE);
        }

        //change mode in lobby
        if (!lobby.changeMode(mode, user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "to many players for new mode");
        }

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
            throw new IllegalInputException(LOBBY_NOT_FOUND_MESSAGE);
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
            throw new IllegalInputException(LOBBY_NOT_FOUND_MESSAGE);
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
            throw new IllegalInputException(LOBBY_NOT_FOUND_MESSAGE);
        }

        //start game
        if (!lobby.createGame(user, gameController)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not the correct number of players");
        }

        return ResponseEntity
            .status(HttpStatus.CREATED).build();
    }

    public void updateLobby(int lobbyId) {
        Lobby lobby = lobbyList.getLobby(lobbyId);
        if (lobby == null) {
            return;
        }
        String path = "/lobbies/" + lobbyId;

        webSocketService.sendMessage(path, new LobbyMessage(lobby), "updateLobby");
    }
}

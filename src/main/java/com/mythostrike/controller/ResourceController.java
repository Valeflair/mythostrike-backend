package com.mythostrike.controller;

import com.mythostrike.model.lobby.Identity;
import com.mythostrike.model.lobby.Mode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
@Slf4j
public class ResourceController {

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

        List<Mode> list = new ArrayList<>();
        List<Identity> identities = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            identities.add(Identity.NOT_SET);
        }
        list.add(new Mode(0,"Free For All","Free for all description",
                2,8,identities));

        identities = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            identities.add(Identity.TEAM_BLUE);
            identities.add(Identity.TEAM_RED);
        }
        list.add(new Mode(1,"1vs1","1vs1 Description",2,2,identities));

        identities = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            identities.add(Identity.TEAM_BLUE);
            identities.add(Identity.TEAM_RED);
        }
        list.add(new Mode(2,"4vs4","4vs4 Description",8,8,identities));

        identities = List.of(Identity.GOD_KING, Identity.GENERAL, Identity.REBEL, Identity.REBEL, Identity.RENEGADE);
        list.add(new Mode(3,"Identity 5","Identity Description",5,5,identities));

        return ResponseEntity.ok(list);
    }

    @GetMapping("/champions")
    public ResponseEntity<Void> getChampions() {
        log.debug("getChampions request");
        //TODO:
        //return ResponseEntity.ok(list);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

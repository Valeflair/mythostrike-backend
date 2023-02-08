package com.mythostrike.model.game;

import com.mythostrike.model.game.player.Identity;
import lombok.Getter;

import java.util.List;

@Getter
public enum Mode {

//TODO:DESCRIPTION

    IDENTITY("identity", 5, 8, "",
        List.of(Identity.GODKING, Identity.GENERAL, Identity.GENERAL, Identity.REBEL, Identity.REBEL, Identity.REBEL,
            Identity.REBEL, Identity.RENEGADE)),
    ONE_VERSUS_ONE("1vs1", 2, 2, "", List.of(Identity.TEAMRED, Identity.TEAMBLUE)),
    TWO_VERSUS_TWO("2vs2", 4, 4, "", List.of(Identity.TEAMRED, Identity.TEAMRED, Identity.TEAMBLUE, Identity.TEAMBLUE)),
    THREE_VERSUS_THREE("3vs3", 6, 6, "",
        List.of(Identity.TEAMRED, Identity.TEAMRED, Identity.TEAMRED, Identity.TEAMBLUE, Identity.TEAMBLUE,
            Identity.TEAMBLUE)),
    FOUR_VERSUS_FOUR("4vs4", 8, 8, "",
        List.of(Identity.TEAMRED, Identity.TEAMRED, Identity.TEAMRED, Identity.TEAMRED, Identity.TEAMBLUE,
            Identity.TEAMBLUE, Identity.TEAMBLUE, Identity.TEAMBLUE));


    private final String name;
    private final int minPlayerCount;
    private final int maxPlayerCount;
    private final String description;
    private final List<Identity> identities;

    Mode(String name, int minPlayerCount, int maxPlayerCount, String description, List<Identity> identities) {
        this.name = name;
        this.minPlayerCount = minPlayerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.description = description;
        this.identities = identities;
    }
}

package com.mythostrike.controller.message.lobby;

import com.mythostrike.model.lobby.Identity;

import java.util.List;

public record ChampionSelectionMessage(String identity, List<ChampionMessage> champions) {

    public ChampionSelectionMessage(Identity identity, List<ChampionMessage> champions) {
        this(identity.toString(), champions);
        //use Identity.toString() because the identity shouldn't be incognito,
        //because the player has to see his own identity in the champion selection
    }

}

package com.mythostrike.controller.message.lobby;

import com.mythostrike.model.lobby.Identity;

import java.util.List;

public record ChampionSelectionMessage(Identity identity, List<ChampionMessage> champions) {

}

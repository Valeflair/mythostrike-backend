package com.mythostrike.controller.message.game;

import com.mythostrike.model.game.Champion;
import com.mythostrike.model.lobby.Identity;

import java.util.List;

public record ChampionSelectionMessage(Identity identity, List<Champion> champions) {
}

package com.mythostrike.controller.message.game;

import java.util.List;

public record UseCardRequest(int lobbyId, int cardId, List<String> targets) {
}

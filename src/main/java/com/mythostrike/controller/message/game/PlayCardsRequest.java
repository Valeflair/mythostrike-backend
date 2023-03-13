package com.mythostrike.controller.message.game;

import java.util.List;

public record PlayCardsRequest(int lobbyId, List<Integer> cardIds, List<String> targets) {
}

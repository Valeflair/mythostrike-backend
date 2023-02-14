package com.mythostrike.controller.message.game;

import java.util.List;

public record SelectCardsRequest(int lobbyId, List<Integer> cardIds, List<String> targets) {
}

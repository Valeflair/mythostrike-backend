package com.mythostrike.controller.message.game;

import java.util.List;

public record DiscardCardRequest(int lobbyId, List<Integer> cardIdList) {
}

package com.mythostrike.controller.message.game;

import java.util.List;

public record CardMoveMessage(String source, String destination, int count, List<Integer> cardsId) {
}

package com.mythostrike.controller.message.game;

import java.util.List;

public record PlayerCondition(List<String> players, int minPlayer, int maxPlayer) {
}

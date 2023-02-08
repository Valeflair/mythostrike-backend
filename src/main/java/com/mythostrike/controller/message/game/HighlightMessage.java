package com.mythostrike.controller.message.game;

import java.util.List;

public record HighlightMessage(List<Integer> cardsId, List<String> players, List<Integer> skillsId, int minCard,
                               int maxCard, int minPlayer, int maxPlayer, String reason) {
}

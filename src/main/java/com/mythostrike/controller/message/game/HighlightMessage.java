package com.mythostrike.controller.message.game;

import lombok.Builder;

import java.util.List;

@Builder
public record HighlightMessage(List<Integer> cardsId,
                               List<String> players,
                               List<Integer> skillsId,
                               int minCard,
                               int maxCard,
                               int minPlayer,
                               int maxPlayer,
                               String reason,
                               boolean optional,
                               boolean needsConfirm) {

}

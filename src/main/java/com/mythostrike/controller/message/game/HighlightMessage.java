package com.mythostrike.controller.message.game;

import lombok.Builder;

import java.util.List;

@Builder
public record HighlightMessage(WebsocketGameMessageType messageType,
                               List<Integer> cardsId,
                               List<String> players,
                               List<Integer> skillsId,
                               int minCard,
                               int maxCard,
                               int minPlayer,
                               int maxPlayer,
                               String reason,
                               boolean optional,
                               boolean needsConfirm) {

    public HighlightMessage(List<Integer> cardsId, List<String> players,
                            List<Integer> skillsId, int minCard, int maxCard, int minPlayer, int maxPlayer,
                            String reason, boolean optional, boolean needsConfirm) {
        this(WebsocketGameMessageType.HIGHLIGHT, cardsId, players, skillsId, minCard, maxCard, minPlayer,
            maxPlayer, reason, optional, needsConfirm);
    }

}

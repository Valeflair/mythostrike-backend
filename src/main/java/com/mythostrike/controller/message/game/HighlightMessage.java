package com.mythostrike.controller.message.game;

import java.util.List;

public record HighlightMessage(WebsocketGameMessageType messageType, List<Integer> cardsId, List<String> players,
                               List<Integer> skillsId, int minCard, int maxCard, int minPlayer, int maxPlayer,
                               String reason, boolean optional, boolean needsToBeConfirmed) {

    public HighlightMessage(List<Integer> cardsId, List<String> players,
                            List<Integer> skillsId, int minCard, int maxCard, int minPlayer, int maxPlayer,
                            String reason, boolean optional, boolean needsToBeConfirmed) {
        this(WebsocketGameMessageType.HIGHLIGHT, cardsId, players, skillsId, minCard, maxCard, minPlayer,
            maxPlayer, reason, optional, needsToBeConfirmed);
    }

}

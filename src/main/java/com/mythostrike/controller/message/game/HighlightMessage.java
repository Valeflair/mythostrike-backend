package com.mythostrike.controller.message.game;

import lombok.Builder;
import java.util.ArrayList;
import java.util.List;

@Builder
public record HighlightMessage(List<Integer> cardsId,
                               int minPlayer,
                               int maxPlayer,
                               List<PlayerCondition> cardPlayerConditions,
                               List<Integer> skillsId,
                               List<PlayerCondition> skillPlayerConditions,
                               String reason,
                               boolean activateEndTurn) {

    public HighlightMessage(List<Integer> cardsId, int minPlayer, int maxPlayer,
                            List<PlayerCondition> cardPlayerConditions,
                            List<Integer> skillsId, List<PlayerCondition> skillPlayerConditions, String reason,
                            boolean activateEndTurn) {
        //TODO: null checks
        this.cardsId = cardsId;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.cardPlayerConditions = cardPlayerConditions;
        this.skillsId = skillsId;
        this.skillPlayerConditions = skillPlayerConditions;
        this.reason = reason;
        this.activateEndTurn = activateEndTurn;
    }

    //make sure the lists are even with the builder not null
    public static class HighlightMessageBuilder {
        public HighlightMessageBuilder builder() {
            this.cardsId = new ArrayList<>();
            this.cardPlayerConditions = new ArrayList<>();
            this.skillPlayerConditions = new ArrayList<>();
            this.skillsId = new ArrayList<>();
            return this;
        }
    }
}

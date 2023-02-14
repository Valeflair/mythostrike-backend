package com.mythostrike.controller.message.game;

import lombok.Builder;
import java.util.ArrayList;
import java.util.List;

@Builder
public record HighlightMessage(List<Integer> cardIds,
                               List<Integer> cardCount,
                               List<PlayerCondition> cardPlayerConditions,
                               List<Integer> skillIds,
                               List<PlayerCondition> skillPlayerConditions,
                               String reason,
                               boolean activateEndTurn) {

    public HighlightMessage(List<Integer> cardIds, List<Integer> cardCount,
                            List<PlayerCondition> cardPlayerConditions,
                            List<Integer> skillIds, List<PlayerCondition> skillPlayerConditions, String reason,
                            boolean activateEndTurn) {
        //TODO: null checks
        this.cardIds = cardIds;
        this.cardCount = cardCount;
        this.cardPlayerConditions = cardPlayerConditions;
        this.skillIds = skillIds;
        this.skillPlayerConditions = skillPlayerConditions;
        this.reason = reason;
        this.activateEndTurn = activateEndTurn;
    }

    //make sure the lists are even with the builder not null
    public static class HighlightMessageBuilder {
        public HighlightMessageBuilder builder() {
            this.cardIds = new ArrayList<>();
            this.cardPlayerConditions = new ArrayList<>();
            this.skillPlayerConditions = new ArrayList<>();
            this.skillIds = new ArrayList<>();
            return this;
        }
    }
}

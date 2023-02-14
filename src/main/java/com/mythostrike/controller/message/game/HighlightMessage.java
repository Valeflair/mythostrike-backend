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
        this.cardIds = new ArrayList<>(cardIds);
        this.cardCount = new ArrayList<>(cardCount);
        this.cardPlayerConditions = new ArrayList<>(cardPlayerConditions);
        this.skillPlayerConditions = new ArrayList<>(skillPlayerConditions);
        this.skillIds = new ArrayList<>(skillIds);
        this.reason = reason;
        this.activateEndTurn = activateEndTurn;

        //make sure lists are not empty
        if (this.cardCount.isEmpty()) {
            this.cardCount.add(0);
        }
        if (this.cardPlayerConditions.isEmpty()) {
            this.cardPlayerConditions.add(new PlayerCondition());
        }
        if (this.skillPlayerConditions.isEmpty()) {
            this.skillPlayerConditions.add(new PlayerCondition());
        }
    }

    //make sure the lists are even with the builder not null
    public static class HighlightMessageBuilder {
        public HighlightMessageBuilder builder() {
            this.cardIds = new ArrayList<>();
            this.cardCount = new ArrayList<>();
            this.cardPlayerConditions = new ArrayList<>();
            this.skillPlayerConditions = new ArrayList<>();
            this.skillIds = new ArrayList<>();
            return this;
        }
    }
}

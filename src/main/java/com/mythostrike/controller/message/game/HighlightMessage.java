package com.mythostrike.controller.message.game;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record HighlightMessage(List<Integer> cardIds,
                               List<Integer> cardCount,
                               List<PlayerCondition> cardPlayerConditions,
                               List<Integer> skillIds,
                               List<Integer> skillCount,
                               List<PlayerCondition> skillPlayerConditions,
                               String reason,
                               boolean activateEndTurn) {

    public HighlightMessage(List<Integer> cardIds, List<Integer> cardCount,
                            List<PlayerCondition> cardPlayerConditions,
                            List<Integer> skillIds, List<Integer> skillCount,
                            List<PlayerCondition> skillPlayerConditions,
                            String reason, boolean activateEndTurn) {

        if (cardIds == null) {
            this.cardIds = new ArrayList<>();
        } else {
            this.cardIds = new ArrayList<>(cardIds);
        }
        if (cardCount == null) {
            this.cardCount = new ArrayList<>(0);
        } else {
            this.cardCount = new ArrayList<>(cardCount);
        }
        if (cardPlayerConditions == null) {
            this.cardPlayerConditions = new ArrayList<>();
        } else {
            this.cardPlayerConditions = new ArrayList<>(cardPlayerConditions);
        }

        if (skillPlayerConditions == null) {
            this.skillPlayerConditions = new ArrayList<>();
        } else {
            this.skillPlayerConditions = new ArrayList<>(skillPlayerConditions);
        }
        if (skillIds == null) {
            this.skillIds = new ArrayList<>();
        } else {
            this.skillIds = new ArrayList<>(skillIds);
        }
        if (skillCount == null) {
            this.skillCount = new ArrayList<>();
        } else {
            this.skillCount = new ArrayList<>(skillCount);
        }

        this.reason = reason;
        this.activateEndTurn = activateEndTurn;

        //make sure lists are not empty
        if (this.cardCount.isEmpty()) {
            this.cardCount.add(0);
        }
        if (this.skillCount.isEmpty()) {
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
            this.skillCount = new ArrayList<>();
            return this;
        }
    }
}

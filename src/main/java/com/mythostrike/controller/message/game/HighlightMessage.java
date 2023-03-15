package com.mythostrike.controller.message.game;

import com.mythostrike.model.game.activity.cards.CardList;
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
    }

    @Override
    public String toString() {
        String sb =
            "HighlightMessage{" + "cardIds=" + cardIds.stream().map(id -> CardList.getCardList().getCard(id)).toList() +
                ", cardCount=" + cardCount +
                ", cardPlayerConditions=" + cardPlayerConditions +
                ", skillIds=" + skillIds +
                ", skillCount=" + skillCount +
                ", skillPlayerConditions=" + skillPlayerConditions +
                ", reason='" + reason + '\'' +
                ", activateEndTurn=" + activateEndTurn +
                '}';
        return sb;
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

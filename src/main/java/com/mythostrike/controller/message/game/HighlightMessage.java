package com.mythostrike.controller.message.game;

import lombok.Builder;
import java.util.ArrayList;
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

    public HighlightMessage(List<Integer> cardsId, List<String> players, List<Integer> skillsId, int minCard,
                            int maxCard, int minPlayer, int maxPlayer, String reason,
                            boolean optional, boolean needsConfirm) {

        if (cardsId == null) {
            this.cardsId = new ArrayList<>();
        } else {
            this.cardsId = new ArrayList<>(cardsId);
        }
        if (players == null) {
            this.players = new ArrayList<>();
        } else {
            this.players = new ArrayList<>(players);
        }
        if (skillsId == null) {
            this.skillsId = new ArrayList<>();
        } else {
            this.skillsId = new ArrayList<>(skillsId);
        }

        this.minCard = minCard;
        this.maxCard = maxCard;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.reason = reason;
        this.optional = optional;
        this.needsConfirm = needsConfirm;
    }

    //make sure the lists are even with the builder not null
    public static class HighlightMessageBuilder {
        public HighlightMessageBuilder builder() {
            this.cardsId = new ArrayList<>();
            this.players = new ArrayList<>();
            this.skillsId = new ArrayList<>();
            return this;
        }
    }
}

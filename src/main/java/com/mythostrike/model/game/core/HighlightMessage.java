package com.mythostrike.model.game.core;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HighlightMessage {
    private List<Integer> cardsId;
    private List<String> players;
    private List<Integer> skillsId;
    private int minCard;
    private int maxCard;
    private int minPlayer;
    private int maxPlayer;
    private String reason;
    private boolean optional;
    private boolean confirmed;

    public HighlightMessage(List<Integer> cardsId, List<String> players, List<Integer> skillsId, int minCard,
                            int maxCard,
                            int minPlayer, int maxPlayer, String reason, boolean optional) {
        this.cardsId = cardsId;
        this.players = players;
        this.skillsId = skillsId;
        this.minCard = minCard;
        this.maxCard = maxCard;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.reason = reason;
        this.optional = optional;
    }
}

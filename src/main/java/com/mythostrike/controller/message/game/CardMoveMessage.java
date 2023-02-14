package com.mythostrike.controller.message.game;

import java.util.ArrayList;
import java.util.List;

public record CardMoveMessage(String source, String destination, int count, List<Integer> cardIds) {

    public CardMoveMessage(String source, String destination, int count, List<Integer> cardIds) {
        this.source = source;
        this.destination = destination;
        this.count = count;
        if (cardIds == null) {
            this.cardIds = new ArrayList<>();
        } else {
            this.cardIds = new ArrayList<>(cardIds);
        }
    }
}

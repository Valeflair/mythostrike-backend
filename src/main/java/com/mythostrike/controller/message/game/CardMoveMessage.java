package com.mythostrike.controller.message.game;

import java.util.ArrayList;
import java.util.List;

public record CardMoveMessage(String source, String destination, int count, List<Integer> cardsId) {

    public CardMoveMessage(String source, String destination, int count, List<Integer> cardsId) {
        this.source = source;
        this.destination = destination;
        this.count = count;
        if (cardsId == null) {
            this.cardsId = new ArrayList<>();
        } else {
            this.cardsId = new ArrayList<>(cardsId);
        }
    }
}

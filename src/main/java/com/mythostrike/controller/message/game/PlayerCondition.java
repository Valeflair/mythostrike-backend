package com.mythostrike.controller.message.game;

import java.util.ArrayList;
import java.util.List;

public record PlayerCondition(List<String> players, List<Integer> count) {

    public PlayerCondition(List<String> players, List<Integer> count) {
        if (players == null) {
            this.players = new ArrayList<>();
        } else {
            this.players = new ArrayList<>(players);
        }
        if (count == null) {
            this.count = new ArrayList<>();
        } else {
            this.count = new ArrayList<>(count);
        }
        if (this.count.isEmpty()) {
            this.count.add(0);
        }
    }

    public PlayerCondition() {
        this(new ArrayList<>(), new ArrayList<>());
    }
}

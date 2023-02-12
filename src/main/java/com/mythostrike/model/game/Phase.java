package com.mythostrike.model.game;


public enum Phase {
    ROUND_START,
    DELAYED_EFFECT,
    DRAW,
    ACTIVE_TURN,
    DISCARD,
    FINISH;

    public static Phase nextPhase(Phase current) {
        int nextOrdinal = (current.ordinal() + 1) % values().length;
        return values()[nextOrdinal];
    }
}

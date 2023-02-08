package com.mythostrike.model.game;


public enum Phase {
    ROUNDSTART,
    DELAYEDEFFECT,
    DRAW,
    ACTIVETURN,
    DISCARD,
    FINISH;

    public static Phase nextPhase(Phase current) {
        int nextOrdinal = (current.ordinal() + 1) % values().length;
        return values()[nextOrdinal];
    }
}

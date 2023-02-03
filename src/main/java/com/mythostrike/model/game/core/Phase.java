package com.mythostrike.model.game.core;


public enum Phase {
    ROUNDSTART,
    DELAYEDEFFECT,
    DRAW,
    ACTIVETURN,
    DISCARD,
    FINISH;

    public static Phase nextPhase(Phase current) {
        int nextOrdinal = (current.ordinal() + 1) % Phase.values().length;
        return Phase.values()[nextOrdinal];
    }
}

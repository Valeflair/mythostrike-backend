package com.mythostrike.model.game.core.activity.events.handle;

public enum DamageType {
    NORMAL("normal"), FIRE("fire"), THUNDER("thunder"), HEAL("heal");

    private final String name;

    DamageType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}

package com.mythostrike.model.game.activity.cards;

import lombok.Getter;

public enum CardSymbol {

    DIAMOND("Diamond"),
    CLUB("Club"),
    HEART("Heart"),
    SPADE("Spade"),
    NO_SYMBOL("NoSymbol");

    @Getter
    private final String name;

    CardSymbol(String name) {
        this.name = name;
    }

    public String getShort() {
        return name.substring(0, 1);
    }


}

package com.mythostrike.model.game.activity.cards;

import lombok.Getter;

@Getter
public enum CardSpaceType {
    DRAW_PILE("drawPile", true),
    DISCARD_PILE("discardPile", false),
    TABLE_PILE("tablePile", false),
    ALL_CARDS("allCards", false),
    HAND_CARDS("handCards", true),
    EQUIPMENT("equipment", false),
    DELAYED_EFFECT("delayedEffect", false);

    private final String name;
    private final boolean isConcealed;

    CardSpaceType(String name, boolean isConcealed) {
        this.name = name;
        this.isConcealed = isConcealed;
    }

}

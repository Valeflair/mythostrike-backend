package com.mythostrike.model.game.activity.cards;

public class HandCards extends CardSpace {

    /**
     * Creates a new hand cards space.
     *
     * @param name The username of the owning player.
     */
    public HandCards(String name) {
        super(CardSpaceType.HAND_CARDS, name);
    }
}

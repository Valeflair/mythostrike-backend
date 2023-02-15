package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.activity.Card;

public class HandCards extends CardSpace {

    /**
     * Creates a new hand cards space.
     *
     * @param name The username of the owning player.
     */
    public HandCards(String name) {
        super(CardSpaceType.HAND_CARDS, name);
    }

    public Card getRandom() {
        return cards.remove(Game.RANDOM_SEED.nextInt(cards.size()));
    }
}

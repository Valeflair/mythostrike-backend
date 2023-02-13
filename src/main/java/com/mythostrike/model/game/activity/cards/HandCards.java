package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.activity.Card;

public class HandCards extends CardSpace {

    public HandCards(String username) {
        super(username);
    }
    public Card getRandom() {
        return cards.remove(Game.RANDOM_SEED.nextInt(cards.size()));
    }
}

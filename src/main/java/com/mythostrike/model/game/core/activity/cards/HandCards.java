package com.mythostrike.model.game.core.activity.cards;

import com.mythostrike.model.game.core.activity.Card;

import java.util.Random;

public class HandCards extends CardSpace {
    public Card getRandom() {
        return cards.remove(new Random().nextInt(cards.size()));
    }
}

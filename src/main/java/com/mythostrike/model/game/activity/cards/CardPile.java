package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.activity.Card;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CardPile extends CardSpace {

    public CardPile(CardSpaceType type) {
        super(type, type.getName());
    }

    public CardPile(CardSpaceType type, List<Card> cards) {
        super(type, type.getName(), cards);
    }

    public void add(Card card, boolean onTop) {
        if (!onTop) {
            cards.add(card);
        } else {
            cards.add(0, card);
        }
    }

    public void shuffle(Random seed) {
        Collections.shuffle(cards, seed);
    }

    public Card subtract() {
        return cards.remove(0);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}


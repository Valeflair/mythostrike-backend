package com.mythostrike.model.game.core.activity.cards;

import com.mythostrike.model.game.core.activity.Card;

import java.util.Collections;
import java.util.List;

public class CardPile extends CardSpace {

    public CardPile() {
        super();
    }

    public CardPile(List<Card> cards) {
        super(cards);
    }

    public void add(Card card, boolean onTop) {
        if (!onTop) {
            cards.add(card);
        } else {
            cards.add(0, card);
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card subtract() {
        return cards.remove(0);
    }
}


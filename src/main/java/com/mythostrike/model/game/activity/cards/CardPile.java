package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.activity.Card;

import java.util.Collections;
import java.util.List;

public class CardPile extends CardSpace {

    public CardPile(String name) {
        super(name);
    }

    public CardPile(String name, List<Card> cards) {
        super(name, cards);
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


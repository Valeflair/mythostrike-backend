package com.mythostrike.model.game.activity.cards;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CardPile extends CardSpace {

    public CardPile(CardSpaceType type) {
        super(type, type.getName());
    }

    public CardPile(CardSpaceType type, List<Card> cards) {
        super(type, type.getName(), cards);
    }


    public void add(Card card, boolean onTop) {
        // Use a HashSet to remove duplicate of cardPile
        Set<Integer> seenIds = new HashSet<>();
        Iterator<Card> iter = cards.iterator();
        while (iter.hasNext()) {
            Card cardIn = iter.next();
            if (seenIds.contains(cardIn.getId())) {
                iter.remove(); // Remove duplicate card
            } else {
                seenIds.add(cardIn.getId()); // Add card ID to seenIds set
            }
        }
        if (cards.contains(card)) {
            return;
        }
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


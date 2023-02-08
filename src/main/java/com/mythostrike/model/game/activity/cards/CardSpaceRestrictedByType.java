package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.activity.Card;

import java.util.HashMap;

public class CardSpaceRestrictedByType extends CardSpace {
    private final HashMap<CardType, Integer> acceptedCards;

    public CardSpaceRestrictedByType(HashMap<CardType, Integer> acceptedCards) {
        this.acceptedCards = acceptedCards;
    }

    @Override
    public void add(Card card) {
        CardType type = card.getType();
        cards.add(card);
        acceptedCards.put(type, acceptedCards.get(type) + 1);
    }

    public boolean accepts(Card card) {
        if (!acceptedCards.containsKey(card.getType())) {
            return false;
        }
        CardType type = card.getType();
        int count = 0;
        for (Card cardInstance : cards) {
            if (cardInstance.getType().equals(type)) {
                count++;
            }
        }
        return count < acceptedCards.get(type);
    }

    @Override
    public void subtractCard(Card card) {
        cards.remove(card);
        CardType type = card.getType();
        acceptedCards.put(type, Math.max(0, acceptedCards.get(type) - 1));
    }

    @Override
    public Card subtractCard(int index) {
        Card card = cards.get(index);
        cards.remove(card);
        CardType type = card.getType();
        acceptedCards.put(type, Math.max(0, acceptedCards.get(type) - 1));
        return card;
    }
}

package com.mythostrike.model.game.activity.cards;

import java.util.HashMap;
import java.util.Map;

public class CardSpaceRestrictedByType extends CardSpace {
    private final HashMap<CardType, Integer> acceptedCards;

    public CardSpaceRestrictedByType(CardSpaceType type, String owner, Map<CardType, Integer> acceptedCards) {
        super(type, String.format("%s-%s", type.getName(), owner));
        this.acceptedCards = new HashMap<>(acceptedCards);
    }

    @Override
    public void add(Card card) {
        if ((!accepts(card))) throw new AssertionError();
        CardType type = card.getType();
        cards.add(card);
        acceptedCards.put(type, acceptedCards.get(type) - 1);
    }

    public boolean accepts(Card card) {
        CardType type = card.getType();
        return acceptedCards.containsKey(type) && acceptedCards.get(type) > 0;
    }

    @Override
    public boolean subtractCard(Card card) {
        if (cards.remove(card)) {
            CardType type = card.getType();
            if (acceptedCards.get(type) == null) throw new AssertionError();
            acceptedCards.put(type, acceptedCards.get(type) + 1);
            return true;
        }
        return false;
    }

    @Override
    public Card subtractCard(int index) {
        Card card = cards.get(index);
        if (!subtractCard(card)) throw new AssertionError();
        return card;
    }
}

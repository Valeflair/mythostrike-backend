package com.mythostrike.model.game.activity.cards;

import java.util.HashMap;
import java.util.Map;

public class CardSpaceRestrictedByName extends CardSpace {
    private final HashMap<String, Integer> acceptedCards;

    public CardSpaceRestrictedByName(CardSpaceType type, String owner, Map<String, Integer> acceptedCards) {
        super(type, String.format("%s-%s", type.getName(), owner));
        this.acceptedCards = new HashMap<>(acceptedCards);
    }


    @Override
    public void add(Card card) {
        if (card == null) throw new NullPointerException("card is null");
        if (cards.contains(card)) return;
        if ((!accepts(card))) throw new AssertionError();
        String name = card.getName();
        cards.add(card);
        acceptedCards.put(name, acceptedCards.get(name) - 1);
    }

    public boolean accepts(Card card) {
        String name = card.getName();
        return acceptedCards.containsKey(name) && acceptedCards.get(name) > 0;

    }

    @Override
    public boolean subtractCard(Card card) {
        if (cards.remove(card)) {
            String name = card.getName();
            if (acceptedCards.get(name) == null) throw new AssertionError();
            acceptedCards.put(name, acceptedCards.get(name) + 1);
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

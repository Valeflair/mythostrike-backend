package com.mythostrike.model.game.core;

import com.mythostrike.model.game.core.activity.Card;

import java.util.ArrayList;

public class CardSpace extends CardList {

    public CardSpace() {
        cards = new ArrayList<Card>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void subtractCard(Card card) {
        cards.remove(card);
    }

    public Card subtractCard(int index) {
        Card card = cards.get(index);
        cards.remove(card);
        return card;
    }

    public int getSum() {
        return cards.size();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}

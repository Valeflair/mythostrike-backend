package com.mythostrike.model.game.core.activity.cards;

import com.mythostrike.model.game.core.activity.Card;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardSpace {

    @Getter
    protected final List<Card> cards;

    public CardSpace() {
        cards = new ArrayList<>();
    }

    public CardSpace(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public void add(Card card) {
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

    public int size() {
        return cards.size();
    }

}

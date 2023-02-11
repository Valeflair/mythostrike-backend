package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.activity.Card;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardSpace cardSpace = (CardSpace) o;
        return cards.equals(cardSpace.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }
}

package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.activity.Card;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CardSpace {

    protected final List<Card> cards;

    @Getter
    private final String name;

    @Getter
    private final CardSpaceType type;

    public CardSpace(CardSpaceType type, String name) {
        this.type = type;
        this.name = name;
        cards = new ArrayList<>();
    }

    public CardSpace(CardSpaceType type, String name, List<Card> cards) {
        this.type = type;
        this.name = name;
        this.cards = new ArrayList<>(cards);
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void addAll(Collection<Card> cards) {
        for (Card card : cards) {
            add(card);
        }
    }

    public void removeAll(List<Card> cards) {
        for (Card card : cards) {
            subtractCard(card);
        }
    }

    /**
     *  Returns a list of cards from the top of the pile. Does not remove them.
     * @param count number of cards in the list
     * @return list of cards, returns null if count > size of pile
     */
    @Nullable
    public List<Card> peekTop(int count) {
        if (count > cards.size()) {
            return null;
        }
        return Collections.unmodifiableList(cards.subList(0, count));
    }

    public Card peekTop() {
        return cards.get(0);
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

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
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

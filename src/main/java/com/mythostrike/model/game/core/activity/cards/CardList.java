package com.mythostrike.model.game.core.activity.cards;

import com.mythostrike.model.game.core.activity.Card;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CardList {
    //TODO ist es notwendig oder soll besser sein
    @Getter
    private static final CardList instance = new CardList(new ArrayList<>(List.of()));
    protected List<Card> cards;

    public CardList() {
        cards = new ArrayList<>();
    }

    public CardList(List<Card> cards) {
        cards = new ArrayList<>(cards);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addCardToBottom(Card card) {
        cards.add(cards.size() - 1, card);
    }

    public Card subtractCard() {
        Card card = cards.get(0);
        cards.remove(card);
        return card;
    }

    public Card subtractCardFromBottom() {
        Card card = cards.get(cards.size() - 1);
        cards.remove(card);
        return card;
    }

    public int getSum() {
        return cards.size();
    }

    public List<Card> getCards() {
        return cards;
    }
}

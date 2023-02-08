package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.activity.Card;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class CardList {
    //TODO ist es notwendig oder soll besser sein
    @Getter
    private static final CardList instance = new CardList(new ArrayList<>(List.of()));
    private final HashMap<Integer, Card> cardMap;
    protected List<Card> cards;

    public CardList() {
        cards = new ArrayList<>();
        cardMap = new HashMap<>();
    }

    public CardList(List<Card> cards) {
        cards = new ArrayList<>(cards);
        cardMap = new HashMap<>();
        for (Card card : cards) {
            cardMap.put(card.getId(), card);
        }
    }

    public Card getCardById(int id) {
        if (!cardMap.containsKey(id)) {
            return null;
        }
        return cardMap.get(id);
    }

    public void addCard(Card card) {
        cards.add(card);
        cardMap.put(card.getId(), card);
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

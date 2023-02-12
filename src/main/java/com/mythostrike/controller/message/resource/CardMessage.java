package com.mythostrike.controller.message.resource;

import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;

public record CardMessage(int id, String name, String description, CardType type, CardSymbol symbol, int point) {
    public CardMessage(Card card) {
        this(card.getId(), card.getName(), card.getDescription(), card.getType(), card.getSymbol(), card.getPoint());
    }
}

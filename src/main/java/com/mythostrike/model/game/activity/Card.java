package com.mythostrike.model.game.activity;


import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.management.GameManager;

import java.util.HashMap;

public abstract class Card extends Activity {
    protected CardType type;
    protected CardSymbol symbol;
    protected int point;

    protected CardMoveHandle cardMoveHandle;
    protected GameManager gameManager;


    protected Card(int id, String name, String description, CardType type, CardSymbol symbol, int point) {
        super(id, name, description);
        this.type = type;
        this.symbol = symbol;
        this.point = point;
    }

    protected Card(Card card) {
        super(card.getId(), card.getName(), card.getDescription());
        this.type = card.getType();
        this.symbol = card.getSymbol();
        this.point = card.getPoint();
    }

    public CardType getType() {
        return type;
    }

    public CardSymbol getSymbol() {
        return symbol;
    }

    public boolean isRed() {
        return symbol.equals(CardSymbol.DIAMOND) || symbol.equals(CardSymbol.HEART);
    }

    public int getPoint() {
        return point;
    }

    public abstract Card deepCopy();

    @Override
    public void activate() {
    }

    public void playOut() {
        gameManager = cardMoveHandle.getGameManager();
        gameManager.getCardManager().moveCard(cardMoveHandle);

        cardMoveHandle.getPlayer().decreaseUseTime(this.getName());
    }
}

package com.mythostrike.model.game.core.activity;


import com.mythostrike.model.game.core.activity.cards.CardSymbol;
import com.mythostrike.model.game.core.activity.cards.CardType;
import com.mythostrike.model.game.core.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.core.management.GameManager;

import java.util.HashMap;

public abstract class Card extends Activity {


    protected CardType type;
    protected CardSymbol symbol;
    protected int point;

    protected CardMoveHandle cardMoveHandle;
    protected GameManager gameManager;


    public Card(int id, String name, String description, CardType type, CardSymbol symbol, int point) {
        super(id, name, description);
        this.type = type;
        this.symbol = symbol;
        this.point = point;
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

    public void activate() { }

    public void playOut() {
        gameManager.getCardManager().moveCard(cardMoveHandle);
        HashMap<String, Integer> restrict = cardMoveHandle.getPlayer().getRestrict();
        restrict.put(this.getName(), restrict.get(this.getName()) - 1);
    }
}

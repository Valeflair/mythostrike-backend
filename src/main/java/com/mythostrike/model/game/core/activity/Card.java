package com.mythostrike.model.game.core.activity;


import com.mythostrike.model.game.core.activity.cards.CardSymbol;
import com.mythostrike.model.game.core.activity.cards.CardType;

public abstract class Card extends Activity {


    private CardType type;
    private CardSymbol symbol;
    private int point;


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
}

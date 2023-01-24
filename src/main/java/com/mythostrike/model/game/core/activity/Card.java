package com.mythostrike.model.game.core.activity;


import com.mythostrike.model.game.core.CardSymbol;
import com.mythostrike.model.game.core.CardType;

public abstract class Card extends Activity {


    private CardType type;
    private CardSymbol symbol;
    private int point;


    public Card(String name, String description, CardType type, CardSymbol symbol, int point) {
        super(name, description);
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

    public int getPoint() {
        return point;
    }


}

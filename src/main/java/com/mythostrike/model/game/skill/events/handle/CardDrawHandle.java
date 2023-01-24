package com.mythostrike.model.game.skill.events.handle;


import com.mythostrike.model.game.core.CardDeck;
import com.mythostrike.model.game.core.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.management.GameManager;

public class CardDrawHandle extends EventHandle {
    int count;
    CardDeck drawDeck;


    public CardDrawHandle(GameManager gameController, Card card, String reason, Player from, int count,
                          CardDeck drawDeck) {
        super(gameController, card, reason, from);
        this.count = count;
        this.drawDeck = drawDeck;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public CardDeck getDrawDeck() {
        return drawDeck;
    }

    public void setDrawDeck(CardDeck drawDeck) {
        this.drawDeck = drawDeck;
    }
}

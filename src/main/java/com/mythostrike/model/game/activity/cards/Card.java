package com.mythostrike.model.game.activity.cards;


import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class Card extends Activity {
    protected CardType type;
    protected CardSymbol symbol;
    protected int point;
    @Setter
    protected PickRequest pickRequest;
    protected CardMoveHandle cardMoveHandle;
    protected GameManager gameManager;
    @Getter
    protected PlayerCondition playerCondition;

    @Getter
    protected CardUseHandle cardUseHandle;


    protected Card(int id, String name, String description, CardType type, CardSymbol symbol, int point) {
        super(name, description);
        this.type = type;
        this.symbol = symbol;
        this.point = point;
        this.id = id;
        this.playerCondition = new PlayerCondition();
    }

    protected Card(Card card) {
        this(card.id, card.name, card.description, card.type, card.symbol, card.point);
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

        //do not play out if it's skill invoked fictional card
        if (point < 0 || id < 0 || symbol.equals(CardSymbol.NO_SYMBOL)) {
            return;
        }
        if (cardMoveHandle == null) {
            cardMoveHandle = new CardMoveHandle(gameManager, "plays card out", cardUseHandle.getPlayer(), null, cardUseHandle.getPlayer().getHandCards(), gameManager.getGame().getTablePile(), List.of(this) );
        }
        gameManager = cardMoveHandle.getGameManager();
        gameManager.getCardManager().moveCard(cardMoveHandle);
        cardMoveHandle.getPlayer().decreaseUseTime(this.getName());
    }

    @Override
    public String toString() {
        return String.format("%s (%s%d)", name, symbol.getShort(), point);
    }
}

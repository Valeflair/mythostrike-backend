package com.mythostrike.model.game.activity.cards;


import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Card extends Activity {
    @Getter
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

    public void playOut() {

        //do not play out if it's skill invoked fictional card or delayed effect and so on


        if (point < 0 || id < 0 || symbol.equals(CardSymbol.NO_SYMBOL)) {
            return;
        }
        if (cardMoveHandle == null) {
            gameManager = cardUseHandle.getGameManager();
            cardMoveHandle = new CardMoveHandle(gameManager, "plays card out", cardUseHandle.getPlayer(), null, cardUseHandle.getPlayer().getHandCards(), gameManager.getGame().getTablePile(), List.of(this) );
        }
        gameManager.output(String.format("%s plays %s out", cardMoveHandle.getPlayer().getUsername(), this.toString()));
        if (!cardUseHandle.getOpponents().isEmpty()
            && (cardUseHandle.getOpponents().size() != 1
            || !cardUseHandle.getOpponents().contains(cardUseHandle.getPlayer()))) {
            gameManager.output(String.format("targets are %s",
                cardUseHandle.getOpponents().stream()
                    .map(Player::getUsername)
                    .collect(Collectors.joining(", "))));
        }
        gameManager.getCardManager().moveCard(cardMoveHandle);
        cardMoveHandle.getPlayer().decreaseUseTime(this.getName());
    }

    public PassiveSkill getPassiveSkill() {
        return null;
    }

    public ActiveSkill getActiveSkill() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s (%s%d)", name, symbol.getShort(), point);
    }
}

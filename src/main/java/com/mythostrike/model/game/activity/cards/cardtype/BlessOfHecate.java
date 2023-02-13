package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.*;
import com.mythostrike.model.game.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class BlessOfHecate extends Card {
    public static final String NAME = "Bless of Hecate";
    public static final String DESCRIPTION = "Draw 2 cards instantly";
    public static final CardType TYPE = CardType.BASIC_CARD;

    private CardUseHandle cardUseHandle;
    private PickRequest pickRequest;


    public BlessOfHecate(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }
    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        gameManager = cardUseHandle.getGameManager();
        Player player = cardUseHandle.getPlayer();
        if (!player.isRestricted(NAME)) {
            this.cardUseHandle = cardUseHandle;
            return true;
        }
        return false;
    }

    @Override
    public BlessOfHecate deepCopy() {
        return new BlessOfHecate(id, symbol, point);
    }

    @Override
    public void activate() {
        Player player = cardUseHandle.getPlayer();
        GameManager gameManager = cardUseHandle.getGameManager();
        //add targetAble enemy into targets

        HighlightMessage highlightMessage = new HighlightMessage(null, null, null, 0,
                0, 1, 1, DESCRIPTION, true, true);
        pickRequest = new PickRequest(player, gameManager, highlightMessage);
        gameManager.queueActivity(this);
        gameManager.queueActivity(pickRequest);
    }


    @Override
    public void use() {
        CardDrawHandle cardDrawHandle = new CardDrawHandle(cardUseHandle.getGameManager(),
                "draw because of using bless of hecate", cardUseHandle.getPlayer(), 2,
                cardUseHandle.getGameManager().getGame().getDrawPile());
        gameManager.getCardManager().drawCard(cardDrawHandle);
    }

}

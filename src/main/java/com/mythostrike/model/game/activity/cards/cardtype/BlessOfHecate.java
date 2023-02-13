package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.handle.DamageType;
import com.mythostrike.model.game.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class BlessOfHecate extends Card {
    public static final String NAME = "Defend";
    public static final String DESCRIPTION = "pick a player as target, he has to play an \"Defend\" or get 1 damage.";
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

    /**
     * player choose this card, highlight all target enemies
     * creates a pickRequest for card user
     */
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

    }

}

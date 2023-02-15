package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.player.Player;

public class BlessOfHecate extends Card {
    public static final String NAME = "Bless of Hecate";
    public static final String DESCRIPTION = "Draw 2 cards instantly";
    public static final CardType TYPE = CardType.BASIC_CARD;



    public BlessOfHecate(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }

    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        gameManager = cardUseHandle.getGameManager();
        Player player = cardUseHandle.getPlayer();
        if (!player.isRestricted(NAME)) {
            this.cardUseHandle = cardUseHandle;
            this.playerCondition = new PlayerCondition();
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
        playOut();
        CardDrawHandle cardDrawHandle = new CardDrawHandle(cardUseHandle.getGameManager(),
            "draw because of using bless of hecate", cardUseHandle.getPlayer(), 2,
            cardUseHandle.getGameManager().getGame().getDrawPile());
        gameManager.getCardManager().drawCard(cardDrawHandle);
    }


}

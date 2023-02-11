package com.mythostrike.model.game.activity.cards.cardtype;


import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Drought extends Card {
    public static final String NAME = "Drought";
    public static final String DESCRIPTION = "pick a player as target, he has to play an \"Defend\" or get 1 damage.";
    public static final CardType TYPE = CardType.BASICCARD;

    private CardUseHandle handle;


    public Drought(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }

    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        gameManager = cardUseHandle.getGameManager();
        Player player = cardUseHandle.getPlayer();
        List<Player> targets = new ArrayList<>();
        for (Player target : cardUseHandle.getGameManager().getGame().getOtherPlayers(player)) {
            if (!target.equals(player) && target.isAlive() && Boolean.FALSE.equals(target.isImmune(NAME))
                && target.getDelayedEffect().accepts(this)) {
                targets.add(target);
            }
        }
        if (!targets.isEmpty() && !player.isRestricted(NAME)) {
            handle = cardUseHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        //TODO:implement

    }

    @Override
    public Drought deepCopy() {
        return new Drought(id, symbol, point);
    }

    //TODO:implement
}

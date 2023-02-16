package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.handle.DamageType;


public class Heal extends Card {

    public static final String NAME = "Heal";
    public static final String DESCRIPTION = "use in active turn to heal you self 1 hp, or use when someone is dying,"
        + " heal him 1 hp";
    public static final CardType TYPE = CardType.BASIC_CARD;


    public Heal(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }

    @Override
    public Heal deepCopy() {
        return new Heal(id, symbol, point);
    }

    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        this.cardUseHandle = cardUseHandle;
        return cardUseHandle.getPlayer().getCurrentHp() < cardUseHandle.getPlayer().getMaxHp()
            && !cardUseHandle.getPlayer().isRestricted(NAME);
    }

    @Override
    public void activate() {
        DamageHandle damageHandle = new DamageHandle(cardUseHandle.getGameManager(), this,
            "use heal to heal hp", cardUseHandle.getPlayer(), cardUseHandle.getPlayer(),
            -1, DamageType.HEAL);
        cardUseHandle.getGameManager().getPlayerManager().applyDamage(damageHandle);
        playOut();
    }
}

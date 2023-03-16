package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.ArmorCard;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.skill.equipment.HeartOfTerraSkill;
import com.mythostrike.model.game.player.Player;

public class HeartOfTerra extends ArmorCard {
    public static final String NAME = "Heart of Terra";
    public static final String DESCRIPTION = "Use this card to equip this armor. When the armor is equipped, "
        + "you are immune to the first damage to your HP. The armor is destroyed after the first damage.";
    public static final CardType TYPE = CardType.ARMOR;

    private final PassiveSkill skill;


    public HeartOfTerra(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, symbol, point);
        skill = new HeartOfTerraSkill(this);
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
    public HeartOfTerra deepCopy() {
        return new HeartOfTerra(id, symbol, point);
    }

    @Override
    public PassiveSkill getPassiveSkill() {
        return skill;
    }
}

package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.ArmorCard;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.skill.equipment.HeartOfTerraSkill;
import com.mythostrike.model.game.activity.skill.equipment.ProtectionOfDianaSkill;
import com.mythostrike.model.game.player.Player;

public class ProtectionOfDiana extends ArmorCard {
    public static final String NAME = "Protection of Diana";
    public static final String DESCRIPTION = "use to equip this armor, you are immune to the Black Attack";
    public static final CardType TYPE = CardType.ARMOR;

    private final PassiveSkill skill;


    public ProtectionOfDiana(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, symbol, point);
        skill = new ProtectionOfDianaSkill();
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

    public ProtectionOfDiana deepCopy() {
        return new ProtectionOfDiana(id, symbol, point);
    }

    @Override
    public PassiveSkill getPassiveSkill() {
        return skill;
    }
}

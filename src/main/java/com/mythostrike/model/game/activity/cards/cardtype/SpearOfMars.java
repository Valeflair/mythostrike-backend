package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.cards.WeaponCard;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.skill.equipment.SpearOfMarsSkill;
import com.mythostrike.model.game.player.Player;

public class SpearOfMars extends WeaponCard {

    public static final String NAME = "Spear of Mars";
    public static final String DESCRIPTION = "Use this card to equip this Weapon. When the Weapon is equipped "
        + "and your Attack is avoided, you can attack again.";
    public static final CardType TYPE = CardType.WEAPON;

    private final PassiveSkill skill;


    public SpearOfMars(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, symbol, point);
        skill = new SpearOfMarsSkill();
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
    public SpearOfMars deepCopy() {
        return new SpearOfMars(id, symbol, point);
    }

    @Override
    public PassiveSkill getPassiveSkill() {
        return skill;
    }
}

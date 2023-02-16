package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.cards.WeaponCard;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.handle.PlayerHandle;
import com.mythostrike.model.game.activity.skill.equipment.SpearOfMarsSkill;
import com.mythostrike.model.game.player.Player;

import java.util.List;
import java.util.Optional;

public class SpearOfMars extends WeaponCard {

    public static final String NAME = "Spear of Mars";
    public static final String DESCRIPTION = "use to equip this weapon, if your attack is avoided by a defend, you can attack again";
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

    public SpearOfMars deepCopy() {
        return new SpearOfMars(id, symbol, point);
    }

    @Override
    public PassiveSkill getPassiveSkill() {
        return skill;
    }
}

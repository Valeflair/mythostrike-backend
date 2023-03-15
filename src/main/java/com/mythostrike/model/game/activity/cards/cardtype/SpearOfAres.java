package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.cards.WeaponCard;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.skill.equipment.SpearOfAresSkill;
import com.mythostrike.model.game.player.Player;

public class SpearOfAres extends WeaponCard {
    public static final String NAME = "Spear of Ares";
    public static final String DESCRIPTION = "Use this card to equip this Weapon. When the Weapon is equipped "
        + "and your last card is an Attack card, you can target up to 3 players.";
    public static final CardType TYPE = CardType.WEAPON;

    private final PassiveSkill skill;


    public SpearOfAres(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, symbol, point);
        skill = new SpearOfAresSkill();
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
    public SpearOfAres deepCopy() {
        return new SpearOfAres(id, symbol, point);
    }

    @Override
    public PassiveSkill getPassiveSkill() {
        return skill;
    }
}

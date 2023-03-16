package com.mythostrike.model.game.activity.skill.equipment;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.cardtype.Attack;
import com.mythostrike.model.game.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

public class SpearOfMarsSkill extends PassiveSkill {

    public static final String NAME = "Spear of Mars Skill";
    public static final String DESCRIPTION = "If your Attack is avoided by a defend, you can attack again";

    private AttackHandle attackHandle;


    public SpearOfMarsSkill() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeAttack.ATTACK_MISSED, this, player, false);
    }

    @Override
    public boolean checkCondition(AttackHandle attackHandle) {

        if (attackHandle.getPlayer().getPassiveSkills().contains(this)) {
            this.attackHandle = attackHandle;
            return true;
        }
        return false;

    }

    @Override
    public void activate() {
        this.attackHandle.getPlayer().increaseUseTime(Attack.NAME);
    }
}

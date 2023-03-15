package com.mythostrike.model.game.activity.skill.equipment;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

public class ProtectionOfDianaSkill extends PassiveSkill {

    public static final String NAME = "Protection of Diana Skill";
    public static final String DESCRIPTION = "you can't be attacked by red attacks";

    private AttackHandle attackHandle;


    public ProtectionOfDianaSkill() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeAttack.ATTACK_EFFECTED, this, player, false);
    }

    @Override
    public boolean checkCondition(AttackHandle attackHandle) {

        if (attackHandle.getOpponent().getPassiveSkills().contains(this)
            && attackHandle.getAttack().isRed()) {
            this.attackHandle = attackHandle;
            return true;
        }
        return false;

    }

    @Override
    public void activate() {
        attackHandle.getGameManager().output("Attack avoided because it it's a red attack against Protection of Diana");
        this.attackHandle.setPrevented(true);
    }


}

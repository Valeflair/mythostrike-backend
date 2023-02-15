package com.mythostrike.model.game.activity.skill.passive;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

public class Strength extends PassiveSkill {

    public static final String NAME = Strength.class.getSimpleName();
    public static final String DESCRIPTION = "when you attack an enemy, he has to play 2 defense to avoid your attack";
    private AttackHandle attackHandle;

    public Strength() {
        super(NAME, DESCRIPTION);
    }


    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeAttack.ATTACK_EFFECTED, this, player, true);

    }

    @Override
    public boolean checkCondition(AttackHandle attackHandle) {
        if (attackHandle.getPlayer().getPassiveSkills().stream().anyMatch(passiveSkill -> name.equals(NAME))) {
            this.attackHandle = attackHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        attackHandle.getDefendAskHandle().setAmount(attackHandle.getDefendAskHandle().getAmount() + 1);
    }

}

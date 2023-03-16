package com.mythostrike.model.game.activity.skill.passive;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeDamage;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

public class Unhealable extends PassiveSkill {

    public static final String NAME = "Unhealable";
    public static final String DESCRIPTION = "You can't be healed.";

    DamageHandle damageHandle;

    public Unhealable() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void register(EventManager eventManager, Player player) {
        player.setPermanentRestrict("Heal", 0);
        eventManager.registerEvent(EventTypeDamage.BEFORE_HP_RECOVER, this, player, true);
    }

    @Override
    public boolean checkCondition(DamageHandle damageHandle) {
        if (damageHandle.getTo().getPassiveSkills().stream()
            .anyMatch(passiveSkill -> passiveSkill.getName().equals(NAME))
            && damageHandle.getDamage() < 0) {
            this.damageHandle = damageHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        damageHandle.setPrevented(true);
    }
}

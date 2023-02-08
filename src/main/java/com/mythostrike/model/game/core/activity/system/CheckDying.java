package com.mythostrike.model.game.core.activity.system;

import com.mythostrike.model.game.core.activity.PassiveSkill;
import com.mythostrike.model.game.core.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.core.activity.events.type.EventTypeDamage;
import com.mythostrike.model.game.core.management.EventManager;
import com.mythostrike.model.game.core.player.Player;

public class CheckDying extends PassiveSkill {
    public static final String NAME = "CheckDying";
    public static final String DESCRIPTION = "if player has low hp";
    public static final int ID = -11;
    private DamageHandle damageHandle;

    public CheckDying() {
        super(ID, NAME, DESCRIPTION);
    }

    @Override
    public boolean checkCondition(DamageHandle damageHandle) {
        return damageHandle.getTo().getCurrentHp() < 1;
    }

    @Override
    public void use() {
        EnterDying enterDying = new EnterDying(damageHandle.getTo(), damageHandle.getGameManager());
        damageHandle.getGameManager().queueActivity(enterDying);
        enterDying.activate();
    }

    /**
     * put it into DamageComplete, so that every player after get damage check if he is about dying
     *
     * @param eventManager the eventManager registering the check dying
     * @param player       doesn't matter, since every player has to check if he is dying
     */
    @Override
    public void register(EventManager eventManager, Player player) {
        for (Player allPlayer : eventManager.getGameManager().getGame().getAllPlayers()) {
            eventManager.registerEvent(EventTypeDamage.DAMAGE_COMPLETE, this, allPlayer, true);
        }
    }
}

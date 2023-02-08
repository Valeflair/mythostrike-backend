package com.mythostrike.model.game.activity.skill.passive;


import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeDamage;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

public class Revenge extends PassiveSkill {
    public static final String NAME = "Revenge";
    public static final String DESCRIPTION = "when you get damage, judge, if not heart, dealer drop "
        + "2 or get damage by you";
    private DamageHandle damageHandle;

    //TODO:implement
    public Revenge(int id) {
        super(id, NAME, DESCRIPTION);
    }


    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeDamage.DAMAGED, this, player, true);
    }

    @Override
    public boolean checkCondition(DamageHandle damageHandle) {
        //TODO
        return true; //damageHandle.getTo().hasSkill("Revenge");
    }

    @Override
    public void activate() {
        //TODO
        //activate skill?
        //hightlightConfirmButton(confirm);
    }

}

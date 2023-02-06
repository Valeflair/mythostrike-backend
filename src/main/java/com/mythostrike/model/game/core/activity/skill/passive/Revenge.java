package com.mythostrike.model.game.core.activity.skill.passive;


import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.activity.PassiveSkill;
import com.mythostrike.model.game.core.management.EventManager;
import com.mythostrike.model.game.core.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.core.activity.events.type.EventTypeDamage;

public class Revenge extends PassiveSkill {
    private DamageHandle damageHandle;

    public static final String NAME = "Revenge";
    public static final String DESCRIPTION = "when you get damage, judge, if not heart, dealer drop "
        + "2 or get damage by you";

    public Revenge(Player player) {
        super(NAME, DESCRIPTION, player);
    }

    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeDamage.DAMAGED, this);
    }

    @Override
    public boolean checkCondition(DamageHandle damageHandle) {
        //TODO
        return true; //damageHandle.getTo().hasSkill("Revenge");
    }

    @Override
    public void clickedConfirmButton(boolean confirm) {
        if (confirm) {
            use();
        }
    }

    @Override
    public void activate() {
        //TODO
        //activate skill?
        //hightlightConfirmButton(confirm);
    }

}

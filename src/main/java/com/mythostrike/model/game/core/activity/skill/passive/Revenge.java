package core.activity.skill.passive;

import core.Player;
import core.activity.PassiveSkill;
import core.management.EventManager;
import skill.events.handle.DamageHandle;
import skill.events.type.EventTypeDamage;

public class Revenge extends PassiveSkill {
    private DamageHandle damageHandle;

    public static final String NAME = "Revenge";
    public static final String DESCRIPTION = "when you get damage, judge, if not heart, dealer drop "
         + "2 or get damage by you";

    public Revenge(Player player) {
        super(NAME, DESCRIPTION, player);
    }

    @Override
    public void register(EventManager eventManager) {
        eventManager.registerEvent(EventTypeDamage.DAMAGED, this);
    }

    @Override
    public boolean checkCondition(DamageHandle damageHandle) {
        return damageHandle.getTo().hasSkill("Revenge");
    }

    @Override
    public void clickedConfirmButton(boolean confirm) {
        if (confirm) { use(); }
    }

    @Override
    public void activate() {
        //activate skill?
        hightlightConfirmButton(confirm);
    }

}

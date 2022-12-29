package Core;

import Events.DamageHandle;
import Events.Event;

import java.util.function.Function;

public enum TriggerSkillData {
    REVENGE("Revenge", Event.DAMAGED, "", true, new Function<DamageHandle, Boolean>() {
        @Override
        public Boolean apply(DamageHandle damageHandle) {
            Damage damage = damageHandle.getDamage();
            Player victim = damage.getTo();
            Player dealer = damage.getFrom();
            Skill skill = victim.hasSkillByName("Revenge");
            if(skill != null){
                GameController gc = damageHandle.getGameController();
                if(gc.askForSkillInvoke(victim, skill)){
                    Card judgeCard = gc.judge();
                    if(judgeCard.getSymbol() != CardSymbol.HEART){
                        if(gc.askForDiscard(dealer, 2, 2, "Drop cause of Revenge", true)){
                        } else {
                            gc.applyDmage(new Damage(victim, dealer, 1,"Revenge",DamageType.NORMAL));
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    });

    private String name;
    private String description;
    private boolean isActive;
    private Function function;
    private Event event;

    TriggerSkillData(String name, String description, boolean isActive, Event event, Function function) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.function = function;
        this.event = event;
    }
}

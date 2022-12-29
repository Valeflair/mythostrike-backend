package Core;

import Events.DamageHandle;
import Events.DamageType;
import Events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum TriggerSkillData {

    REVENGE("Revenge", "whenever you get damage, judge a card, if it's not heart, " +
            "damage dealer has to drop 2 handcards or get 1 damage dealded by you"
            ,true,new ArrayList<Event>(List.of(Event.DAMAGED)) , new Function<DamageHandle, Boolean>() {
        @Override
        public Boolean apply(DamageHandle damageHandle) {

            Player victim = damageHandle.getTo();
            Player dealer = damageHandle.getFrom();
            Skill<?> skill = victim.hasSkillByName("Revenge");
            if(skill != null){
                GameController gc = damageHandle.getGameController();
                if(gc.askForSkillInvoke(victim, skill)){
                    Card judgeCard = gc.judge();
                    if(judgeCard.getSymbol() != CardSymbol.HEART){
                        if (gc.askForDiscard(dealer, dealer.getHandCards(), 2, 2, true, "Drop 2 HandCard cause of Revenge, or you can dont drop and get 1 Damage")){
                        } else {
                            gc.applyDamage(new DamageHandle(victim, dealer,null,1,"Revenge", DamageType.NORMAL, gc));
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    })
    ;
    private final String name;
    private final String description;
    private final boolean isActive;
    private final Function<?,?> function;
    private final ArrayList<Event> events;

    TriggerSkillData(String name, String description, boolean isActive, ArrayList<Event> events, Function<?,?> function) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.function = function;
        this.events = events;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public Function<?,?> getFunction() {
        return function;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}

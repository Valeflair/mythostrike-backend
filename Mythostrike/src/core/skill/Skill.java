package core.skill;


import core.game.Effect;
import core.game.management.EventManager;
import core.skill.events.handle.*;

public class Skill {
    private final String name;
    private final String description;
    private final boolean isActive;
    private final Effect<?> effect;

    public Skill(String name, String description, boolean isActive, Effect<?> effect) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.effect = effect;
    }

    public boolean register(EventManager eventManager) {return false;}


    public boolean trigger(AttackHandle handle){return false;}
    public boolean trigger(CardAskHandle handle){return false;}
    public boolean trigger(CardMoveHandle handle){return false;}
    public boolean trigger(CardUseHandle handle){return false;}
    public boolean trigger(DamageHandle handle){return false;}
    public boolean trigger(PhaseChangeHandle handle){return false;}
    public boolean trigger(PhaseHandle handle){return false;}
    public boolean trigger(PlayerHandle handle){return false;}

    public boolean condition(AttackHandle handle){return false;}
    public boolean condition(CardAskHandle handle){return false;}
    public boolean condition(CardMoveHandle handle){return false;}
    public boolean condition(CardUseHandle handle){return false;}
    public boolean condition(DamageHandle handle){return false;}
    public boolean condition(PhaseChangeHandle handle){return false;}
    public boolean condition(PhaseHandle handle){return false;}
    public boolean condition(PlayerHandle handle){return false;}

    public boolean activate(AttackHandle handle){return false;}
    public boolean activate(CardAskHandle handle){return false;}
    public boolean activate(CardMoveHandle handle){return false;}
    public boolean activate(CardUseHandle handle){return false;}
    public boolean activate(DamageHandle handle){return false;}
    public boolean activate(PhaseChangeHandle handle){return false;}
    public boolean activate(PhaseHandle handle){return false;}
    public boolean activate(PlayerHandle handle){return false;}


    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public boolean isActive() {
        return isActive;
    }




}

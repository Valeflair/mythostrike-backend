package core.skill;

import core.game.Effect;
import core.game.management.EventManager;
import core.skill.events.handle.EventHandle;
import core.skill.events.type.EventType;

public class PassiveSkill<T extends EventHandle> extends Skill {
    EventType type;
    Effect<T> effect;
    public PassiveSkill(String name, String description, boolean isActive, Effect<T> effect, EventType type) {
        super(name, description, isActive, effect);
        this.effect = effect;
    }

    public boolean register(EventManager eventManager) {

        eventManager.registerEvent(type, effect);

        return false;
    }

}

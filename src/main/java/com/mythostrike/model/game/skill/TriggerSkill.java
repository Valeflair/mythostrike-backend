package com.mythostrike.model.game.skill;

import com.mythostrike.model.game.core.Effect;
import com.mythostrike.model.game.core.management.EventManager;
import com.mythostrike.model.game.skill.events.handle.EventHandle;
import com.mythostrike.model.game.skill.events.type.EventType;

import java.util.function.Function;

public class TriggerSkill<T extends EventHandle, R extends EventType> extends Skill {
    private final Effect<T> effect;
    private final R eventType;

    public TriggerSkill(String name, String description, boolean isActive, Function<EventManager, Boolean> register,
                        Effect<T> effect, R eventType) {
        super(name, description, isActive, register);
        this.effect = effect;
        this.eventType = eventType;
    }

    public Effect<T> getEffect() {
        return effect;
    }

    public R getEventType() {
        return eventType;
    }
}

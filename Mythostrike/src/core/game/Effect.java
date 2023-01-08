package core.game;

import core.skill.events.handle.EventHandle;
import core.skill.events.type.EventType;

import java.util.function.Function;

public class Effect<T extends EventHandle> {


    private Function<T, Boolean> condition;
    private Function<T, Boolean> activation;
    private Function<T, Boolean> function;
    private EventType eventType;


    public Effect() { }


    public Effect(EventType eventType, Function<T, Boolean> condition, Function<T, Boolean> activation, Function<T, Boolean> function) {
        this.eventType = eventType;
        this.condition = condition;
        this.activation = activation;
        this.function = function;
    }


    public Boolean checkCondition(T handle) {
        return condition.apply(handle);
    }
    public Boolean effect(T handle) {
        return function.apply(handle);
    }

    public EventType getEventType() {
        return eventType;
    }

}

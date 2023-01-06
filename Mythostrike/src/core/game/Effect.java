package core.game;

import core.skill.events.handle.EventHandle;

import java.util.function.Function;

public class Effect<T extends EventHandle> {


    private Function<T, Boolean> condition;
    private Function<T, Boolean> activation;
    private Function<T, Boolean> function;

    private final String name;


    public Effect(String name, Function<T, Boolean> condition, Function<T, Boolean> activation, Function<T, Boolean> function) {
        this.name = name;
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


}

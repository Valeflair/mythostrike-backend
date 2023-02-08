package com.mythostrike.model.game;

import com.mythostrike.model.game.activity.events.handle.EventHandle;

import java.util.function.Function;

public class Effect<T extends EventHandle> {
    private final Function<T, Boolean> alwaysTrue = new Function<T, Boolean>() {

        @Override
        public Boolean apply(T handle) {
            return true;
        }
    };
    private final Function<T, Boolean> alwaysFalse = new Function<T, Boolean>() {

        @Override
        public Boolean apply(T handle) {
            return false;
        }
    };

    private final Function<T, Boolean> condition;
    private final Function<T, Boolean> function;

    public Effect(Function<T, Boolean> condition, Function<T, Boolean> function) {
        this.condition = condition;
        this.function = function;
    }

    public Effect(boolean conditionAlways, Function<T, Boolean> function) {
        this.function = function;
        if (conditionAlways) {
            condition = alwaysTrue;
        } else {
            condition = alwaysFalse;
        }
    }

    public Effect(Function<T, Boolean> condition, boolean functionAlways) {
        this.condition = condition;
        if (functionAlways) {
            function = alwaysTrue;
        } else {
            function = alwaysFalse;
        }
    }

    public Effect() {
        function = alwaysFalse;
        condition = alwaysFalse;
    }

    public Effect(boolean conditionAlways, boolean functionAlways) {


        if (conditionAlways) {
            condition = alwaysTrue;
        } else {
            condition = alwaysFalse;
        }
        if (functionAlways) {
            function = alwaysTrue;
        } else {
            function = alwaysFalse;
        }
    }

    public Boolean checkCondition(T handle) {
        return condition.apply(handle);
    }

    public Boolean effect(T handle) {
        return function.apply(handle);
    }

}

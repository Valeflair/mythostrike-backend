package Events.Observers;

import Events.EventType;

import java.util.EventListener;
import java.util.function.Function;

public class FunctionObserver<T> implements EventListener {
    Function<T, Boolean> function;
    EventType eventType;

    public FunctionObserver(EventType eventType, Function<T, Boolean> function) {
        this.eventType = eventType;
        this.function = function;
    }

    public boolean onEvent(T handle){
        return function.apply(handle);
    };



}

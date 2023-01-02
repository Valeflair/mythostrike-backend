package skill.events;

import java.util.EventListener;
import java.util.function.Function;

public class FunctionObserver<T> implements EventListener {
    Function<T, Boolean> function;

    public FunctionObserver(Function<T, Boolean> function) {
        this.function = function;
    }

    public boolean onEvent(T handle){
        return function.apply(handle);
    };



}

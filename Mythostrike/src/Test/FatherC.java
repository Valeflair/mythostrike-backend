package Test;

import java.util.function.Function;

public class FatherC<T>{
    private final Function<T,Boolean> function;

    public FatherC(Function<T, Boolean> function) {
        this.function = function;
    }
    public boolean apply(T param){
        return function.apply(param);
    }
}

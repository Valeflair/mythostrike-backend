package Test;

import java.util.function.Function;

public class ChildC extends FatherC<ChildB>{

    public ChildC(Function<ChildB, Boolean> function) {
        super(function);
    }
}

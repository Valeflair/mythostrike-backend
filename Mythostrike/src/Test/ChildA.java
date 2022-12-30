package Test;

public class ChildA implements FatherA{

    public void trigger(ChildB childB) {
        System.out.println("FuncA");
    }
    public void trigger(FatherB fatherB) {
        System.out.println("NOTHING");
    }
}

package Test;

public class temp {



    public static void main(String[] args) {
        Father a = new Father();
        Father b = new Child();
        a.meh();
        b.meh();
    }
}
class Father {
    public void meh() {
        System.out.println("father");
    }
}

class Child extends Father {
    @Override
    public void meh() {
        System.out.println("child");
    }
}
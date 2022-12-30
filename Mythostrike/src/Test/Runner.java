package Test;

public class Runner {
    public void run(){
        //data
        FatherB fatherB = new ChildB();
        //data
        ChildB childB = new ChildB();
        //implementation
        ChildA childA = new ChildA();
        //interface
        FatherA fatherA = new ChildA();


        fatherA.trigger(childB);
        childA.trigger(childB);
        fatherA.trigger(fatherB);
        childA.trigger(fatherB);
        System.out.println(fatherA instanceof ChildA);
        System.exit(0);
    }
}

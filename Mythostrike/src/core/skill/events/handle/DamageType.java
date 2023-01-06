package core.skill.events.handle;

public enum DamageType {
    NORMAL("normal"),FIRE("fire"),THUNDER("thunder");

    private final String name;

    DamageType(String name){
        this.name = name;
    };

    public String toString(){
        return name;
    }
}

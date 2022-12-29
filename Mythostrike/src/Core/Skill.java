package Core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;



public class Skill{
    private String name;
    private String description;
    private boolean isActive;
    private Function function;

    public Skill(String name, String description, boolean isActive, Function function) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.function = function;
    }

    public Skill(TriggerSkillData data){

    }

    public void run() throws InvocationTargetException, IllegalAccessException {
        function.apply(null);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean equals(Skill skill){
        return this.name.equals(skill.getName());
    }
    public boolean equals(String name){
        return this.name.equals(name);
    }
}

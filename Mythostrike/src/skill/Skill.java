package skill;


import core.management.EventManager;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Skill {
    private String name;
    private String description;
    private boolean isActive;
    private Function<EventManager, Boolean> register;

    public Skill(String name, String description, boolean isActive, Function<EventManager, Boolean> register) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.register = register;
    }
    public boolean init(EventManager eventManager) {
        return register.apply(eventManager);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean equals(Skill skill) {
        return this.name.equals(skill.getName());
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }



}

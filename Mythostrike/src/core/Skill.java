package core;

import java.util.EventListener;


public class Skill {
    private String name;
    private String description;
    private boolean isActive;
    private EventListener listener;

    public Skill(TriggerSkillData data) {
        this.name = data.getName();
        this.description = data.getDescription();
        this.isActive = data.isActive();
        this.listener = data.getFunctionListener();

    }

    public void initialEvent(){
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

    public boolean equals(Skill skill) {
        return this.name.equals(skill.getName());
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }



}

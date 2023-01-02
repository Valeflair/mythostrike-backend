package skill;


import core.management.EventManager;

public abstract class Skill {
    private String name;
    private String description;
    private boolean isActive;

    public Skill(TriggerSkillData data) {
        this.name = data.getName();
        this.description = data.getDescription();
        this.isActive = data.isActive();
    }

    public Skill(String name, String description, boolean isActive) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public abstract void initialEvent(EventManager eventManager);

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

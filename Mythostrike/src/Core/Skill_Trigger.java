package Core;

import Events.Event;

import java.util.ArrayList;
import java.util.function.Function;

public class Skill_Trigger extends Skill<Skill_Trigger> {
    ArrayList<Event> events;

    public Skill_Trigger(String name, String description, Function<?,?> function) {
        super(name, description, function);
    }

    public Skill_Trigger(String name, String description, Function<?,?> function, ArrayList<Event> events) {
        super(name, description, function);
        this.events = events;
    }

    public Skill_Trigger(TriggerSkillData data) {
        super(data);
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}

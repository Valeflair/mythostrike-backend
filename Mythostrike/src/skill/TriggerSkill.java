package skill;

import core.Game;
import core.Player;
import core.management.EventManager;
import skill.events.Event;
import skill.events.EventType;
import skill.events.FunctionObserver;
import skill.events.handle.PlayerHandle;

import java.lang.reflect.Type;
import java.util.List;

public class TriggerSkill<T> extends Skill {
    private final FunctionObserver<?> functionListener;
    private final List<EventType> events;


    public TriggerSkill(TriggerSkillData data) {
        super(data);
        this.functionListener = data.getFunctionListener();
        this.events = data.getEvents();
    }


    public TriggerSkill(TriggerSkillData data, FunctionObserver<T> functionListener, List<EventType> events) {
        super(data);
        this.functionListener = functionListener;
        this.events = events;
    }

    public TriggerSkill(String name, String description, boolean isActive, FunctionObserver<T> functionListener, List<EventType> events) {
        super(name, description, isActive);
        this.functionListener = functionListener;
        this.events = events;
    }

    @Override
    public void initialEvent(EventManager eventManager) {
        for (EventType eventType : events) {
            eventManager.getEvent(eventType);
        }

    }


    public void initial(EventManager eventManager) {

        for (EventType eventType : events) {
            //TODO
        }
    }
}

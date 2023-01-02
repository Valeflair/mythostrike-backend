package skill.events;

import skill.events.handle.EventHandle;

import java.util.ArrayList;

public class Event<T> {
    private EventType type;
    private final ArrayList<FunctionObserver<T>> listeners;


    public Event(EventType type) {
        this.type = type;
        listeners = new ArrayList<>();
    }

    public void addToListener(FunctionObserver<T> observer) {
        listeners.add(observer);
    }
    public void removeFromListener(FunctionObserver<T> observer) {
        listeners.remove(observer);
    }

    public void onEvent(T handle) {
        for (FunctionObserver<T> listener : listeners) {
            listener.onEvent(handle);
        }
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public ArrayList<FunctionObserver<T>> getListeners() {
        return listeners;
    }
}

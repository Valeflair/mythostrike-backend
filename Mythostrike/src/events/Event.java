package events;

public class Event<T> {
    private EventType type;
    private Listener<T> listener;

    public Event(EventType type){
        this.type = type;
        listener = new Listener<T>();
    }

    public void addToListener(FunctionObserver<T> observer) {
        listener.addListener(observer);
    }
    public void removeFromListener(FunctionObserver<T> observer){
        listener.removeListener(observer);
    }

    public void onEvent(T handle){
        listener.trigger(handle);
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Listener<T> getListener() {
        return listener;
    }

    public void setListener(Listener<T> listener) {
        this.listener = listener;
    }
}

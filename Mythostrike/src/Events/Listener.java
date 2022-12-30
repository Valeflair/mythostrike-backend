package Events;

import Events.Observers.FunctionObserver;

import java.util.ArrayList;

public class Listener<T> {

    final ArrayList<FunctionObserver<T>> listeners = new ArrayList<>();
    public void trigger(T handle){

        for (FunctionObserver<T> listener : listeners){
            listener.onEvent(handle);
        }

    }



    public void addListener(FunctionObserver<T> listener){
        listeners.add(listener);
    }
    public void removeListener(FunctionObserver<T> listener){
        listeners.remove(listener);
    }


}

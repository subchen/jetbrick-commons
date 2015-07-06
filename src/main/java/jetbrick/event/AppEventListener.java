package jetbrick.event;

import java.util.EventListener;

public interface AppEventListener<T extends AppEvent> extends EventListener {

    /**
     * You can throw exception in a synchronized listener.
     */
    void onAppEvent(T event);

}

package jetbrick.event;

import java.util.EventObject;

public class AppEvent extends EventObject {

    public AppEvent(Object source) {
        super(source);
    }

    // null value for @AppListener
    static class NULL extends AppEvent {
        public NULL(Object source) {
            super(source);
        }
    }
}

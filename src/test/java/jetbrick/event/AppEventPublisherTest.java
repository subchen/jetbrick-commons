package jetbrick.event;

import org.junit.Assert;
import org.junit.Test;

public class AppEventPublisherTest {

    @Test
    public void testAddEventListener() {
        Listener1 listener = new Listener1();
        AppEventPublisher.addEventListener(listener);

        AppEvent e1 = new Event1("test");
        AppEventPublisher.publishEvent(e1);

        Assert.assertEquals(listener.event, e1);
    }

    static class Event1 extends AppEvent {
        public Event1(String source) {
            super(source);
        }
    }

    @AppListener(async = false)
    static class Listener1 implements AppEventListener<Event1> {
        private Event1 event;

        @Override
        public void onAppEvent(Event1 event) {
            this.event = event;
        }
    }
}

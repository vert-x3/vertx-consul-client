package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Event;
import io.vertx.ext.consul.EventOptions;
import org.junit.Test;

import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Events extends ConsulTestBase {

    @Test
    public void testEvents1() {
        String name = "custom-event";
        EventOptions opts = new EventOptions().setPayload("content");
        Event event = getAsync(h -> writeClient.fireEventWithOptions(name, opts, h));
        assertEquals(name, event.getName());
        assertEquals(opts.getPayload(), event.getPayload());
        String evId = event.getId();
        List<Event> list = getAsync(h -> writeClient.listEvents(h));
        long cnt = list.stream().map(Event::getId).filter(id -> id.equals(evId)).count();
        assertEquals(cnt, 1);
    }

}

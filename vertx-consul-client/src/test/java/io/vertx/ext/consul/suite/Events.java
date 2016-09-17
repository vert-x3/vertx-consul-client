package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Event;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.handleResult;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Events extends ConsulTestBase {

    @Test
    public void testEvents1() throws InterruptedException {
        writeClient.fireEvent(Event.empty().withName("custom-event").withPayload("content"), handleResult(h1 -> {
            assertEquals(h1.getName(), "custom-event");
            assertEquals(h1.getPayload(), "content");
            String evId = h1.getId();
            writeClient.listEvents(handleResult(h2 -> {
                long cnt = h2.stream().map(Event::getId).filter(id -> id.equals(evId)).count();
                assertEquals(cnt, 1);
                testComplete();
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

}

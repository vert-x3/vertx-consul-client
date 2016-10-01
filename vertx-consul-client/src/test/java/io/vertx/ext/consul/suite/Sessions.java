package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.*;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Sessions extends ConsulTestBase {

    @Test
    public void createAndDestroy() {
        Session opt = new Session()
                .setTtl("1h")
                .setLockDelay("42s");
        String id = getAsync(h -> writeClient.createSession(opt, h));
        Session session = getAsync(h -> writeClient.infoSession(id, h));
        assertEquals(opt.getLockDelay(), session.getLockDelay());
        assertEquals(opt.getTtl(), session.getTtl());
        assertEquals("serfHealth", session.getChecks().get(0));
        runAsync(h -> writeClient.destroySession(id, h));
    }

    @Test
    public void deleteBehavior() {
        String id = getAsync(h -> writeClient.createSession(new Session().setTtl("1h").setBehavior("delete"), h));
        assertTrue(getAsync(h -> writeClient.putValueWithOptions("foo/bar", "value1", new KeyValueOptions().setAcquireSession(id), h)));
        KeyValue pair = getAsync(h -> writeClient.getValue("foo/bar", h));
        assertEquals("value1", pair.getValue());
        assertEquals(id, pair.getSession());
        runAsync(h -> writeClient.destroySession(id, h));
        writeClient.getValue("foo/bar", h -> {
            if (h.failed()) {
                testComplete();
            }
        });
        await(1, TimeUnit.SECONDS);
    }
}

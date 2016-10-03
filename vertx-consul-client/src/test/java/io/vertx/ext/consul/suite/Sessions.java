package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Sessions extends ConsulTestBase {

    @Test
    public void sessionOptionsSerialization() {
        SessionOptions options = new SessionOptions()
                .setBehavior(SessionBehavior.RELEASE)
                .setChecks(Arrays.asList("c1", "c2"))
                .setLockDelay(42)
                .setName("optName")
                .setNode("optNode")
                .setTtl(442);
        SessionOptions restored = new SessionOptions(options.toJson());
        assertEquals(options.getBehavior(), restored.getBehavior());
        assertEquals(options.getChecks(), restored.getChecks());
        assertEquals(options.getLockDelay(), restored.getLockDelay());
        assertEquals(options.getName(), restored.getName());
        assertEquals(options.getNode(), restored.getNode());
        assertEquals(options.getTtl(), restored.getTtl());
    }

    @Test
    public void createAndDestroy() {
        SessionOptions opt = new SessionOptions()
                .setLockDelay(42)
                .setTtl(442);
        String id = getAsync(h -> writeClient.createSession(opt, h));
        Session session = getAsync(h -> writeClient.infoSession(id, h));
        assertEquals(opt.getLockDelay(), session.getLockDelay());
        assertEquals("serfHealth", session.getChecks().get(0));
        List<Session> list = getAsync(h -> writeClient.listSessions(h));
        assertEquals(session.getId(), list.get(0).getId());
        List<Session> nodeSesions = getAsync(h -> writeClient.listNodeSessions(session.getNode(), h));
        assertEquals(session.getId(), nodeSesions.get(0).getId());
        runAsync(h -> writeClient.destroySession(id, h));
    }

    @Test
    public void deleteBehavior() {
        String id = getAsync(h -> writeClient.createSession(new SessionOptions().setTtl(442).setBehavior(SessionBehavior.DELETE), h));
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

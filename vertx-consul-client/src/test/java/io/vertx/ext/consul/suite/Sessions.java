package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Session;
import org.junit.Test;

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
}

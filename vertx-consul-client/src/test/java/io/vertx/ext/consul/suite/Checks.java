package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.CheckInfo;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Checks extends ConsulTestBase {

    @Test
    public void ttlCheckLifecycle() {
        String checkId = "checkId";
        CheckOptions opts = CheckOptions.ttl("1s")
                .setId(checkId)
                .setName("checkName");
        runAsync(h -> writeClient.registerCheck(opts, h));

        CheckInfo checkInfo;

        runAsync(h -> writeClient.warnCheck(new CheckOptions().setId(checkId).setNote("warn"), h));
        checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.warning, checkInfo.getStatus());
        assertEquals("warn", checkInfo.getOutput());

        runAsync(h -> writeClient.failCheck(new CheckOptions().setId(checkId).setNote("fail"), h));
        checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());
        assertEquals("fail", checkInfo.getOutput());

        runAsync(h -> writeClient.passCheck(new CheckOptions().setId(checkId).setNote("pass"), h));
        checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.passing, checkInfo.getStatus());
        assertEquals("pass", checkInfo.getOutput());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());

        runAsync(h -> writeClient.deregisterCheck(checkId, h));
    }

    private CheckInfo getCheckInfo(String id) {
        List<CheckInfo> checks = getAsync(h -> writeClient.localChecks(h));
        return checks.stream()
                .filter(check -> check.getId().equals(id))
                .findFirst()
                .get();
    }
}

package io.vertx.ext.consul.suite;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.CheckInfo;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.handleResult;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Checks extends ConsulTestBase {

    @Test
    public void ttlCheckLifecycle() {
        String checkId = "checkId";
        registerCheck(CheckOptions.ttl("1s")
                .setId(checkId)
                .setName("checkName"));

        CheckInfo checkInfo;

        updateStatus(new CheckOptions().setId(checkId).setNote("warn"), CheckInfo.Status.warning);
        checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.warning, checkInfo.getStatus());
        assertEquals("warn", checkInfo.getOutput());

        updateStatus(new CheckOptions().setId(checkId).setNote("fail"), CheckInfo.Status.critical);
        checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());
        assertEquals("fail", checkInfo.getOutput());

        updateStatus(new CheckOptions().setId(checkId).setNote("pass"), CheckInfo.Status.passing);
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

        deregisterCheck(checkId);
    }

    private void registerCheck(CheckOptions check) {
        CountDownLatch latch = new CountDownLatch(1);
        writeClient.registerCheck(check, handleResult(v -> latch.countDown()));
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {}
    }

    private void deregisterCheck(String id) {
        CountDownLatch latch = new CountDownLatch(1);
        writeClient.deregisterCheck(id, handleResult(v -> latch.countDown()));
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {}
    }

    private void updateStatus(CheckOptions check, CheckInfo.Status status) {
        CountDownLatch latch = new CountDownLatch(1);
        Handler<AsyncResult<Void>> resultHandler = handleResult(v -> latch.countDown());
        switch (status) {
            case passing: writeClient.passCheck(check, resultHandler); break;
            case warning: writeClient.warnCheck(check, resultHandler); break;
            case critical: writeClient.failCheck(check, resultHandler); break;
        }
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {}
    }

    private CheckInfo getCheckInfo(String id) {
        CountDownLatch latch = new CountDownLatch(1);
        CheckInfo[] cons = new CheckInfo[1];
        writeClient.localChecks(handleResult(h -> {
            Optional<CheckInfo> opt = h.stream().filter(check -> check.getId().equals(id)).findFirst();
            if (opt.isPresent()) {
                cons[0] = opt.get();
                latch.countDown();
            }
        }));
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {}
        return cons[0];
    }
}

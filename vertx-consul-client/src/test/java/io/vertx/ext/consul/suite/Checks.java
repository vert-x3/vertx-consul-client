package io.vertx.ext.consul.suite;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.consul.CheckInfo;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

        sleep(1500);

        checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());

        runAsync(h -> writeClient.deregisterCheck(checkId, h));
    }

    @Test
    public void httpCheckLifecycle() {
        HealthReporter reporter = new HealthReporter(vertx);

        String checkId = "checkId";
        CheckOptions opts = CheckOptions.http("http://localhost:" + reporter.port(), "1s")
                .setId(checkId)
                .setName("checkName");
        runAsync(h -> writeClient.registerCheck(opts, h));

        sleep(1500);
        CheckInfo checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.passing, checkInfo.getStatus());

        reporter.setStatus(CheckInfo.Status.warning);
        sleep(1500);
        checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.warning, checkInfo.getStatus());

        reporter.setStatus(CheckInfo.Status.critical);
        sleep(1500);
        checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());

        reporter.close();

        runAsync(h -> writeClient.deregisterCheck(checkId, h));
    }

    @Test
    public void tcpCheckLifecycle() {
        HealthReporter reporter = new HealthReporter(vertx);

        String checkId = "checkId";
        CheckOptions opts = CheckOptions.tcp("localhost:" + reporter.port(), "1s")
                .setId(checkId)
                .setName("checkName");
        runAsync(h -> writeClient.registerCheck(opts, h));

        sleep(1500);
        CheckInfo checkInfo = getCheckInfo(checkId);
        assertEquals(CheckInfo.Status.passing, checkInfo.getStatus());

        reporter.close();
        sleep(1500);
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

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class HealthReporter {

        private final HttpServer server;
        private final int port;

        private CheckInfo.Status status = CheckInfo.Status.passing;

        HealthReporter(Vertx vertx) {
            this.port = getFreePort();
            CountDownLatch latch = new CountDownLatch(1);
            this.server = vertx.createHttpServer().requestHandler(h -> h.response()
                    .setStatusCode(statusCode(status))
                    .end(status.name())).listen(port, h -> latch.countDown());
            try {
                latch.await(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int port() {
            return port;
        }

        void setStatus(CheckInfo.Status status) {
            this.status = status;
        }

        void close() {
            server.close();
        }

        private int statusCode(CheckInfo.Status status) {
            switch (status) {
                case passing: return 200;
                case warning: return 429;
                default: return 500;
            }
        }

        private static int getFreePort() {
            int port = -1;
            try {
                ServerSocket socket = new ServerSocket(0);
                port = socket.getLocalPort();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return port;
        }
    }
}

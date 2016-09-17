package io.vertx.ext.consul;

import io.vertx.core.DeploymentOptions;
import org.junit.BeforeClass;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulServiceTest extends ConsulTestSuite {

    private static AtomicInteger cnt = new AtomicInteger(1);

    @BeforeClass
    public static void initClient() {
        ConsulTestBase.clientCreator = (vertx, config) -> {
            CountDownLatch latch = new CountDownLatch(1);
            String addr = "vertx.consul." + cnt.incrementAndGet();
            DeploymentOptions options = new DeploymentOptions().setConfig(config.put("address", addr));
            ConsulClient[] client = { null };
            vertx.deployVerticle("service:io.vertx.consul-service", options, Utils.handleResult(id -> {
                client[0] = ConsulService.createEventBusProxy(vertx, addr);
                latch.countDown();
            }));
            try {
                latch.await(10L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return client[0];
        };
        ConsulTestBase.clientCloser = client -> {};
    }

}

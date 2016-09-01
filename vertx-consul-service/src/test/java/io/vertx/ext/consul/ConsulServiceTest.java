package io.vertx.ext.consul;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulServiceTest extends ConsulClientTestBase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        JsonObject config = config();
        DeploymentOptions options = new DeploymentOptions().setConfig(config);
        CountDownLatch latch = new CountDownLatch(1);
        vertx.deployVerticle("service:io.vertx.consul-service", options, onSuccess(id -> {
            client = ConsulService.createEventBusProxy(vertx, "vertx.consul");
            latch.countDown();
        }));
        awaitLatch(latch);
    }
}

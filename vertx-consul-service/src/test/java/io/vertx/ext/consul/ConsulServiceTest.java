package io.vertx.ext.consul;

import io.vertx.core.DeploymentOptions;

import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulServiceTest extends ConsulTestBase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        CountDownLatch latch = new CountDownLatch(2);
        DeploymentOptions masterOptions = new DeploymentOptions().setConfig(config(TEST_TOKEN));
        vertx.deployVerticle("service:io.vertx.consul-service", masterOptions, onSuccess(id -> {
            masterClient = ConsulService.createEventBusProxy(vertx, "vertx.consul");
            latch.countDown();
        }));
        DeploymentOptions testOptions = new DeploymentOptions().setConfig(config(TEST_TOKEN));
        vertx.deployVerticle("service:io.vertx.consul-service", testOptions, onSuccess(id -> {
            testClient = ConsulService.createEventBusProxy(vertx, "vertx.consul");
            latch.countDown();
        }));
        awaitLatch(latch);
    }
}

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
        DeploymentOptions masterOptions = new DeploymentOptions().setConfig(config(MASTER_TOKEN).put("address", "consul.master.client"));
        vertx.deployVerticle("service:io.vertx.consul-service", masterOptions, onSuccess(id -> {
            masterClient = ConsulService.createEventBusProxy(vertx, "consul.master.client");
            latch.countDown();
        }));
        DeploymentOptions testOptions = new DeploymentOptions().setConfig(config(TEST_TOKEN).put("address", "consul.test.client"));
        vertx.deployVerticle("service:io.vertx.consul-service", testOptions, onSuccess(id -> {
            testClient = ConsulService.createEventBusProxy(vertx, "consul.test.client");
            latch.countDown();
        }));
        awaitLatch(latch);
    }
}

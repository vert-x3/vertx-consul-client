package io.vertx.ext.consul;

import io.vertx.ext.consul.impl.ConsulClientImpl;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientTest extends ConsulTestBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        masterClient = new ConsulClientImpl(vertx, config(MASTER_TOKEN));
        testClient = new ConsulClientImpl(vertx, config(TEST_TOKEN));
    }

    @Override
    public void tearDown() throws Exception {
        masterClient.close();
        testClient.close();
        super.tearDown();
    }
}

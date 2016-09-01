package io.vertx.ext.consul;

import io.vertx.ext.consul.impl.ConsulClientImpl;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientTest extends ConsulClientTestBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        client = new ConsulClientImpl(vertx, config());
    }

    @Override
    public void tearDown() throws Exception {
        client.close();
        super.tearDown();
    }
}

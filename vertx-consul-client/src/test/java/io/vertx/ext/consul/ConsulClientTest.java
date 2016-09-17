package io.vertx.ext.consul;

import io.vertx.ext.consul.impl.ConsulClientImpl;
import org.junit.BeforeClass;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientTest extends ConsulTestSuite {

    @BeforeClass
    public static void initCreator() {
        ConsulTestBase.clientCreator = ConsulClientImpl::new;
        ConsulTestBase.clientCloser = ConsulClient::close;
    }

}

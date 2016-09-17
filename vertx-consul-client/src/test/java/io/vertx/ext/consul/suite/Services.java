package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.handleResult;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Services extends ConsulTestBase {

    @Test
    public void testService1() throws InterruptedException {
        ServiceOptions service = new ServiceOptions()
                .setName("serviceName")
                .setTags(Arrays.asList("tag1", "tag2"))
                .setCheckOptions(CheckOptions.ttl("10s"))
                .setAddress("10.0.0.1")
                .setPort(8080);
        waitFor(2);
        testClient.registerService(service, handleResult(h1 -> {
            testClient.localServices(handleResult(h2 -> {
                ServiceInfo s = h2.stream().filter(i -> "serviceName".equals(i.getName())).findFirst().get();
                assertEquals(s.getTags().get(1), "tag2");
                assertEquals(s.getAddress(), "10.0.0.1");
                assertEquals(s.getPort(), 8080);
                complete();
            }));
            testClient.localChecks(handleResult(h2 -> {
                CheckInfo c = h2.stream().filter(i -> "serviceName".equals(i.getServiceName())).findFirst().get();
                assertEquals(c.getId(), "service:serviceName");
                complete();
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

    @Test
    public void testService2() {
        testClient.infoService("consul", handleResult(services -> {
            long cnt = services.stream().filter(s -> s.getName().equals("consul")).count();
            assertEquals(cnt, 1);
            testComplete();
        }));
        await(1, TimeUnit.SECONDS);
    }

}

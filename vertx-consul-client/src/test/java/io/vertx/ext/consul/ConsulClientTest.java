package io.vertx.ext.consul;

import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import com.pszymczyk.consul.LogLevel;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.impl.ConsulClientImpl;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientTest extends VertxTestBase {

    private ConsulProcess consul;
    private ConsulClient client;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        consul = ConsulStarterBuilder.consulStarter()
                .withLogLevel(LogLevel.ERR)
                .build()
                .start();
        JsonObject config = new JsonObject()
                .put("host", "localhost")
                .put("port", consul.getHttpPort());
        client = new ConsulClientImpl(vertx, config);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        client.close();
        consul.close();
    }

    @Test
    public void test1() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        client.putValue("key11", "value11", h1 -> {
            client.getValue("key11", h2 -> {
                KeyValuePair pair = h2.result();
                assertEquals("key11", pair.getKey());
                assertEquals("value11", pair.getValue());
                latch.countDown();
            });
        });
        latch.await(1, TimeUnit.SECONDS);
    }

    @Test
    public void test2() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        client.putValue("key11", "value11", h1 -> {
            client.putValue("key12", "value12", h2 -> {
                client.getValues("key1", h3 -> {
                    JsonArray expected = new JsonArray().add("value11").add("value12");
                    assertEquals(expected, h3.result());
                    latch.countDown();
                });
            });
        });
        latch.await(1, TimeUnit.SECONDS);
    }
}

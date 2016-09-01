package io.vertx.ext.consul;

import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import com.pszymczyk.consul.LogLevel;
import io.vertx.core.json.JsonObject;
import io.vertx.test.core.VertxTestBase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientTestBase extends VertxTestBase {

    private static ConsulProcess consul;
    protected ConsulClient client;

    protected JsonObject config() {
        return new JsonObject()
                .put("consul_host", "localhost")
                .put("consul_port", consul.getHttpPort());
    }

    @BeforeClass
    public static void startConsul() {
        consul = ConsulStarterBuilder.consulStarter()
                .withLogLevel(LogLevel.ERR)
                .build()
                .start();
    }

    @AfterClass
    public static void stopConsul() {
        consul.close();
    }

    @Test
    public void test1() throws InterruptedException {
        client.putValue("key11", "value11", h1 -> {
            client.getValue("key11", h2 -> {
                KeyValuePair pair = h2.result();
                assertEquals("key11", pair.getKey());
                assertEquals("value11", pair.getValue());
                testComplete();
            });
        });
        await();
    }

    @Test
    public void test2() throws InterruptedException {
        client.putValue("key11", "value11", h1 -> {
            client.putValue("key12", "value12", h2 -> {
                client.getValues("key1", h3 -> {
                    List<KeyValuePair> expected = Arrays.asList(
                            new KeyValuePair("key11", "value11"),
                            new KeyValuePair("key12", "value12")
                    );
                    assertEquals(expected, h3.result());
                    testComplete();
                });
            });
        });
        await();
    }
}

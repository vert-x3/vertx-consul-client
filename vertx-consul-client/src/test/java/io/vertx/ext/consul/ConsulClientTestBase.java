package io.vertx.ext.consul;

import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import com.pszymczyk.consul.LogLevel;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
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
    protected static final String MASTER_TOKEN = "topSecret";
    protected static final String DC = "test-dc";
    protected ConsulClient client;

    protected JsonObject config() {
        return new JsonObject()
                .put("acl_token", MASTER_TOKEN)
                .put("consul_host", "localhost")
                .put("consul_port", consul.getHttpPort());
    }

    @BeforeClass
    public static void startConsul() {
        consul = ConsulStarterBuilder.consulStarter()
                .withLogLevel(LogLevel.ERR)
                .withCustomConfig(new JsonObject()
                        .put("datacenter", DC)
                        .put("acl_master_token", MASTER_TOKEN)
                        .put("acl_datacenter", DC).encode())
                .build()
                .start();
    }

    @AfterClass
    public static void stopConsul() {
        consul.close();
    }

    @Test
    public void test1() throws InterruptedException {
        client.putValue("key11", "value11", handleResult(h1 -> {
            client.getValue("key11", handleResult(pair -> {
                assertEquals("key11", pair.getKey());
                assertEquals("value11", pair.getValue());
                testComplete();
            }));
        }));
        await();
    }

    @Test
    public void test2() throws InterruptedException {
        client.putValue("key11", "value11", handleResult(h1 -> {
            client.putValue("key12", "value12", handleResult(h2 -> {
                client.getValues("key1", handleResult(h3 -> {
                    List<KeyValuePair> expected = Arrays.asList(
                            new KeyValuePair("key11", "value11"),
                            new KeyValuePair("key12", "value12")
                    );
                    assertEquals(expected, h3);
                    testComplete();
                }));
            }));
        }));
        await();
    }

    @Test
    public void test3() throws InterruptedException {
        client.createAclToken(h -> {
            String id = h.result();
            client.infoAclToken(id, handleResult(info -> {
                assertEquals(id, info.getString("ID"));
                testComplete();
            }));
        });
        await();
    }

    @Test
    public void test4() throws InterruptedException {
        client.createAclToken(h -> {
            String id = h.result();
            client.destroyAclToken(id, handleResult(destroyed -> testComplete()));
        });
        await();
    }

    protected <T> Handler<AsyncResult<T>> handleResult(Handler<T> resultHandler) {
        return h -> {
            if (h.succeeded()) {
                resultHandler.handle(h.result());
            } else {
                throw new RuntimeException(h.cause());
            }
        };
    }
}

package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.KeyValuePair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.handleResult;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class KVStore extends ConsulTestBase {

    @Test
    public void testKV1() throws InterruptedException {
        testClient.putValue("foo/bar", "value", handleResult(h1 -> {
            testClient.getValue("foo/bar", handleResult(pair -> {
                assertEquals("foo/bar", pair.getKey());
                assertEquals("value", pair.getValue());
                testComplete();
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

    @Test
    public void testKV2() throws InterruptedException {
        testClient.putValue("foo/bars1", "value1", handleResult(h1 -> {
            testClient.putValue("foo/bars2", "value2", handleResult(h2 -> {
                testClient.getValues("foo/bars", handleResult(h3 -> {
                    List<KeyValuePair> expected = Arrays.asList(
                            new KeyValuePair("foo/bars1", "value1"),
                            new KeyValuePair("foo/bars2", "value2")
                    );
                    assertEquals(expected, h3);
                    testComplete();
                }));
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

    @Test
    public void testKV3() throws InterruptedException {
        testClient.putValue("foo/toDel", "value", handleResult(h1 -> {
            testClient.deleteValue("foo/toDel", handleResult(h2 -> {
                testClient.getValue("foo/toDel", h3 -> {
                    if (h3.failed()) {
                        testComplete();
                    }
                });
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

}

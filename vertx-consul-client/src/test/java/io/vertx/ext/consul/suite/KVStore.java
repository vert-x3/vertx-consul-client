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
    public void readClientCantWriteOneValue() {
        readClient.putValue("foo/bar1", "value1", h -> {
            assertTrue(h.failed());
            testComplete();
        });
    }

    @Test
    public void readClientCanReadOneValue() {
        writeClient.putValue("foo/bar2", "value2", handleResult(h1 -> {
            readClient.getValue("foo/bar2", handleResult(h2 -> {
                assertEquals(h2, new KeyValuePair("foo/bar2", "value2"));
                writeClient.deleteValue("foo/bar2", h3 -> testComplete());
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

    @Test
    public void writeClientHaveFullAccessToOneValue() throws InterruptedException {
        writeClient.putValue("foo/bar3", "value3", handleResult(h1 -> {
            writeClient.getValue("foo/bar3", handleResult(h2 -> {
                assertEquals("foo/bar3", h2.getKey());
                assertEquals("value3", h2.getValue());
                writeClient.deleteValue("foo/bar3", h3 -> testComplete());
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

    @Test
    public void readClientCanReadValues() throws InterruptedException {
        writeClient.putValue("foo/bars1", "value1", handleResult(h1 -> {
            writeClient.putValue("foo/bars2", "value2", handleResult(h2 -> {
                readClient.getValues("foo/bars", handleResult(h3 -> {
                    List<KeyValuePair> expected = Arrays.asList(
                            new KeyValuePair("foo/bars1", "value1"),
                            new KeyValuePair("foo/bars2", "value2")
                    );
                    assertEquals(expected, h3);
                    writeClient.deleteValues("foo/bars", h4 -> testComplete());
                }));
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

    @Test
    public void writeClientHaveFullAccessToFewValues() throws InterruptedException {
        writeClient.putValue("foo/bars3", "value3", handleResult(h1 -> {
            writeClient.putValue("foo/bars4", "value4", handleResult(h2 -> {
                writeClient.getValues("foo/bars", handleResult(h3 -> {
                    List<KeyValuePair> expected = Arrays.asList(
                            new KeyValuePair("foo/bars3", "value3"),
                            new KeyValuePair("foo/bars4", "value4")
                    );
                    assertEquals(expected, h3);
                    writeClient.deleteValues("foo/bars", h4 -> testComplete());
                }));
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

    @Test
    public void clientCantDeleteUnknownKey() throws InterruptedException {
        writeClient.putValue("foo/toDel", "value", handleResult(h1 -> {
            writeClient.deleteValue("foo/toDel", handleResult(h2 -> {
                writeClient.getValue("foo/toDel", h3 -> {
                    if (h3.failed()) {
                        testComplete();
                    }
                });
            }));
        }));
        await(1, TimeUnit.SECONDS);
    }

}

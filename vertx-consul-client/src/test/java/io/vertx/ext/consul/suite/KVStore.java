package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.KeyValuePair;
import io.vertx.ext.consul.Utils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class KVStore extends ConsulTestBase {

    @Test(expected = RuntimeException.class)
    public void readClientCantWriteOneValue() {
        runAsync(h -> readClient.putValue("foo/bar1", "value1", h));
    }

    @Test
    public void readClientCanReadOneValue() {
        runAsync(h -> writeClient.putValue("foo/bar2", "value2", h));
        KeyValuePair pair = getAsync(h -> readClient.getValue("foo/bar2", h));
        assertEquals(new KeyValuePair("foo/bar2", "value2"), pair);
        runAsync(h -> writeClient.deleteValue("foo/bar2", h));
    }

    @Test
    public void writeClientHaveFullAccessToOneValue() {
        runAsync(h -> writeClient.putValue("foo/bar3", "value3", h));
        KeyValuePair pair = getAsync(h -> writeClient.getValue("foo/bar3", h));
        assertEquals("foo/bar3", pair.getKey());
        assertEquals("value3", pair.getValue());
        runAsync(h -> writeClient.deleteValue("foo/bar3", h));
    }

    @Test
    public void readClientCanReadValues() {
        runAsync(h -> writeClient.putValue("foo/bars1", "value1", h));
        runAsync(h -> writeClient.putValue("foo/bars2", "value2", h));
        List<KeyValuePair> list = getAsync(h -> readClient.getValues("foo/bars", h));
        List<KeyValuePair> expected = Arrays.asList(
                new KeyValuePair("foo/bars1", "value1"),
                new KeyValuePair("foo/bars2", "value2")
        );
        assertEquals(expected, list);
        runAsync(h -> writeClient.deleteValues("foo/bars", h));
    }

    @Test
    public void writeClientHaveFullAccessToValues() {
        runAsync(h -> writeClient.putValue("foo/bars3", "value3", h));
        runAsync(h -> writeClient.putValue("foo/bars4", "value4", h));
        List<KeyValuePair> list = getAsync(h -> writeClient.getValues("foo/bars", h));
        List<KeyValuePair> expected = Arrays.asList(
                new KeyValuePair("foo/bars3", "value3"),
                new KeyValuePair("foo/bars4", "value4")
        );
        assertEquals(expected, list);
        runAsync(h -> writeClient.deleteValues("foo/bars", h));
    }

    @Test(expected = RuntimeException.class)
    public void clientCantDeleteUnknownKey() {
        runAsync(h -> writeClient.putValue("foo/toDel", "value", h));
        runAsync(h -> writeClient.deleteValue("foo/toDel", h));
        Utils.<KeyValuePair>getAsync(h -> writeClient.getValue("foo/toDel", h));
    }

}

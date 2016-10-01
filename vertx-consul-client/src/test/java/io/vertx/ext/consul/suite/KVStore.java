package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.KeyValue;
import io.vertx.ext.consul.KeyValueOptions;
import io.vertx.ext.consul.Utils;
import org.junit.Test;

import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class KVStore extends ConsulTestBase {

    @Test(expected = RuntimeException.class)
    public void readClientCantWriteOneValue() {
        Utils.<Boolean>getAsync(h -> readClient.putValue("foo/bar1", "value1", h));
    }

    @Test
    public void readClientCanReadOneValue() {
        assertTrue(getAsync(h -> writeClient.putValue("foo/bar2", "value2", h)));
        KeyValue pair = getAsync(h -> readClient.getValue("foo/bar2", h));
        assertEquals("foo/bar2", pair.getKey());
        assertEquals("value2", pair.getValue());
        runAsync(h -> writeClient.deleteValue("foo/bar2", h));
    }

    @Test
    public void writeClientHaveFullAccessToOneValue() {
        KeyValueOptions opts = new KeyValueOptions().setFlags(42);
        assertTrue(getAsync(h -> writeClient.putValueWithOptions("foo/bar3", "value3", opts, h)));
        KeyValue pair = getAsync(h -> writeClient.getValue("foo/bar3", h));
        assertEquals("foo/bar3", pair.getKey());
        assertEquals("value3", pair.getValue());
        assertEquals(opts.getFlags(), pair.getFlags());
        runAsync(h -> writeClient.deleteValue("foo/bar3", h));
    }

    @Test
    public void readClientCanReadValues() {
        String prefix = "foo/bars";
        assertTrue(getAsync(h -> writeClient.putValue(prefix + "1", "value1", h)));
        assertTrue(getAsync(h -> writeClient.putValue(prefix + "2", "value2", h)));
        List<KeyValue> list = getAsync(h -> readClient.getValues(prefix, h));
        assertEquals(prefix + "1", list.get(0).getKey());
        assertEquals("value1", list.get(0).getValue());
        assertEquals(prefix + "2", list.get(1).getKey());
        assertEquals("value2", list.get(1).getValue());
        runAsync(h -> writeClient.deleteValues(prefix, h));
    }

    @Test
    public void writeClientHaveFullAccessToValues() {
        String prefix = "foo/bars";
        assertTrue(getAsync(h -> writeClient.putValue(prefix + "3", "value3", h)));
        assertTrue(getAsync(h -> writeClient.putValue(prefix + "4", "value4", h)));
        List<KeyValue> list = getAsync(h -> writeClient.getValues(prefix, h));
        assertEquals(prefix + "3", list.get(0).getKey());
        assertEquals("value3", list.get(0).getValue());
        assertEquals(prefix + "4", list.get(1).getKey());
        assertEquals("value4", list.get(1).getValue());
        runAsync(h -> writeClient.deleteValues(prefix, h));
    }

    @Test
    public void canSetAllFlags() {
        KeyValueOptions opts = new KeyValueOptions().setFlags(-1);
        assertTrue(getAsync(h -> writeClient.putValueWithOptions("foo/bar", "value", opts, h)));
        KeyValue pair = getAsync(h -> readClient.getValue("foo/bar", h));
        assertEquals("foo/bar", pair.getKey());
        assertEquals("value", pair.getValue());
        assertEquals(opts.getFlags(), pair.getFlags());
        runAsync(h -> writeClient.deleteValue("foo/bar", h));
    }

    @Test
    public void checkAndSet() {
        assertTrue(getAsync(h -> writeClient.putValue("foo/bar4", "value4", h)));
        KeyValue pair;
        pair = getAsync(h -> readClient.getValue("foo/bar4", h));
        long index1 = pair.getModifyIndex();
        assertTrue(getAsync(h -> writeClient.putValue("foo/bar4", "value4.1", h)));
        pair = getAsync(h -> readClient.getValue("foo/bar4", h));
        long index2 = pair.getModifyIndex();
        assertTrue(index2 > index1);
        assertFalse(getAsync(h -> writeClient.putValueWithOptions("foo/bar4", "value4.1", new KeyValueOptions().setCasIndex(index1), h)));
        assertTrue(getAsync(h -> writeClient.putValueWithOptions("foo/bar4", "value4.1", new KeyValueOptions().setCasIndex(index2), h)));
        runAsync(h -> writeClient.deleteValue("foo/bar4", h));
    }

    @Test(expected = RuntimeException.class)
    public void clientCantDeleteUnknownKey() {
        runAsync(h -> writeClient.deleteValue("unknown", h));
    }

}

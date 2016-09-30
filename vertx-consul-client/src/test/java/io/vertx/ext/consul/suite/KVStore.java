package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.KeyValuePair;
import io.vertx.ext.consul.KeyValuePairOptions;
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
        KeyValuePairOptions opts = new KeyValuePairOptions().setKey("foo/bar1").setValue("value1");
        Utils.<Boolean>getAsync(h -> readClient.putValue(opts, h));
    }

    @Test
    public void readClientCanReadOneValue() {
        KeyValuePairOptions opts = new KeyValuePairOptions().setKey("foo/bar2").setValue("value2");
        assertTrue(getAsync(h -> writeClient.putValue(opts, h)));
        KeyValuePair pair = getAsync(h -> readClient.getValue(opts.getKey(), h));
        assertEquals(opts.getKey(), pair.getKey());
        assertEquals(opts.getValue(), pair.getValue());
        runAsync(h -> writeClient.deleteValue(opts.getKey(), h));
    }

    @Test
    public void writeClientHaveFullAccessToOneValue() {
        KeyValuePairOptions opts = new KeyValuePairOptions().setKey("foo/bar3").setValue("value3").setFlags(42);
        assertTrue(getAsync(h -> writeClient.putValue(opts, h)));
        KeyValuePair pair = getAsync(h -> writeClient.getValue(opts.getKey(), h));
        assertEquals(opts.getKey(), pair.getKey());
        assertEquals(opts.getValue(), pair.getValue());
        assertEquals(opts.getFlags(), pair.getFlags());
        runAsync(h -> writeClient.deleteValue(opts.getKey(), h));
    }

    @Test
    public void readClientCanReadValues() {
        String prefix = "foo/bars";
        KeyValuePairOptions opts1 = new KeyValuePairOptions().setKey(prefix + "1").setValue("value1");
        KeyValuePairOptions opts2 = new KeyValuePairOptions().setKey(prefix + "2").setValue("value2");
        assertTrue(getAsync(h -> writeClient.putValue(opts1, h)));
        assertTrue(getAsync(h -> writeClient.putValue(opts2, h)));
        List<KeyValuePair> list = getAsync(h -> readClient.getValues(prefix, h));
        assertEquals(opts1.getKey(), list.get(0).getKey());
        assertEquals(opts1.getValue(), list.get(0).getValue());
        assertEquals(opts2.getKey(), list.get(1).getKey());
        assertEquals(opts2.getValue(), list.get(1).getValue());
        runAsync(h -> writeClient.deleteValues(prefix, h));
    }

    @Test
    public void writeClientHaveFullAccessToValues() {
        String prefix = "foo/bars";
        KeyValuePairOptions opts1 = new KeyValuePairOptions().setKey(prefix + "3").setValue("value3");
        KeyValuePairOptions opts2 = new KeyValuePairOptions().setKey(prefix + "4").setValue("value4");
        assertTrue(getAsync(h -> writeClient.putValue(opts1, h)));
        assertTrue(getAsync(h -> writeClient.putValue(opts2, h)));
        List<KeyValuePair> list = getAsync(h -> writeClient.getValues(prefix, h));
        assertEquals(opts1.getKey(), list.get(0).getKey());
        assertEquals(opts1.getValue(), list.get(0).getValue());
        assertEquals(opts2.getKey(), list.get(1).getKey());
        assertEquals(opts2.getValue(), list.get(1).getValue());
        runAsync(h -> writeClient.deleteValues(prefix, h));
    }

    @Test
    public void canSetAllFlags() {
        KeyValuePairOptions opts = new KeyValuePairOptions()
                .setKey("foo/bar")
                .setValue("value")
                .setFlags(-1);
        assertTrue(getAsync(h -> writeClient.putValue(opts, h)));
        KeyValuePair pair = getAsync(h -> readClient.getValue(opts.getKey(), h));
        assertEquals(opts.getKey(), pair.getKey());
        assertEquals(opts.getValue(), pair.getValue());
        assertEquals(opts.getFlags(), pair.getFlags());
        runAsync(h -> writeClient.deleteValue(opts.getKey(), h));
    }

    @Test(expected = RuntimeException.class)
    public void clientCantDeleteUnknownKey() {
        runAsync(h -> writeClient.deleteValue("unknown", h));
    }

}

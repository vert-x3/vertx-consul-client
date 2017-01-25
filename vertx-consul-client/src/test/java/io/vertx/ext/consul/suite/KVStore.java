/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.*;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

import static io.vertx.ext.consul.Utils.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class KVStore extends ConsulTestBase {

  @Test(expected = RuntimeException.class)
  public void readClientCantWriteOneValue() {
    Utils.<Boolean>getAsync(h -> readClient.putValue("foo/bar1", "value1", h));
  }

  @Test
  public void readClientEmptyValue() {
    assertTrue(getAsync(h -> writeClient.putValue("foo/bar/empty", "", h)));
    KeyValue pair = getAsync(h -> readClient.getValue("foo/bar/empty", h));
    assertEquals("foo/bar/empty", pair.getKey());
    assertEquals("", pair.getValue());
    runAsync(h -> writeClient.deleteValue("foo/bar/empty", h));
  }

  @Test
  public void readClientCanReadOneValue() {
    assertTrue(getAsync(h -> writeClient.putValue("foo/bar/?воля", "इच्छाशक्ति", h)));
    KeyValue pair = getAsync(h -> readClient.getValue("foo/bar/?воля", h));
    assertEquals("foo/bar/?воля", pair.getKey());
    assertEquals("इच्छाशक्ति", pair.getValue());
    runAsync(h -> writeClient.deleteValue("foo/bar/?воля", h));
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
    KeyValueList list = getAsync(h -> readClient.getValues(prefix, h));
    assertEquals(prefix + "1", list.getList().get(0).getKey());
    assertEquals("value1", list.getList().get(0).getValue());
    assertEquals(prefix + "2", list.getList().get(1).getKey());
    assertEquals("value2", list.getList().get(1).getValue());
    runAsync(h -> writeClient.deleteValues(prefix, h));
  }

  @Test
  public void writeClientHaveFullAccessToValues() {
    String prefix = "foo/bars";
    assertTrue(getAsync(h -> writeClient.putValue(prefix + "3", "value3", h)));
    assertTrue(getAsync(h -> writeClient.putValue(prefix + "4", "value4", h)));
    KeyValueList list = getAsync(h -> writeClient.getValues(prefix, h));
    assertEquals(prefix + "3", list.getList().get(0).getKey());
    assertEquals("value3", list.getList().get(0).getValue());
    assertEquals(prefix + "4", list.getList().get(1).getKey());
    assertEquals("value4", list.getList().get(1).getValue());
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

  @Test
  public void canGetValueBlocking() throws InterruptedException {
    blockingQuery(() -> {
      KeyValue pair = getAsync(h -> readClient.getValue("foo/barBlock", h));
      return pair.getModifyIndex();
    });
  }

  @Test
  public void canGetValuesBlocking() throws InterruptedException {
    blockingQuery(() -> {
      KeyValueList list = getAsync(h -> readClient.getValues("foo/barBlock", h));
      return list.getIndex();
    });
  }

  private void blockingQuery(Supplier<Long> indexSupplier) throws InterruptedException {
    assertTrue(getAsync(h -> writeClient.putValue("foo/barBlock", "valueBlock1", h)));
    long consulIndex = indexSupplier.get();
    CountDownLatch latch = new CountDownLatch(2);
    readClient.getValueWithOptions("foo/barBlock", new BlockingQueryOptions().setIndex(consulIndex), h -> {
      assertEquals(h.result().getValue(), "valueBlock2");
      assertTrue(h.result().getModifyIndex() > consulIndex);
      latch.countDown();
    });
    readClient.getValuesWithOptions("foo/bar", new BlockingQueryOptions().setIndex(consulIndex), h -> {
      assertEquals(h.result().getList().size(), 1);
      assertTrue(h.result().getIndex() > consulIndex);
      latch.countDown();
    });
    sleep(vertx, 2000);
    assertEquals(latch.getCount(), 2);
    assertTrue(getAsync(h -> writeClient.putValue("foo/barBlock", "valueBlock2", h)));
    awaitLatch(latch);

    runAsync(h -> writeClient.deleteValue("foo/barBlock", h));
  }
}

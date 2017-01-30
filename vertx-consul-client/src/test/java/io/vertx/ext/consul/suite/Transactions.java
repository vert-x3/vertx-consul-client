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

import java.util.List;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Transactions extends ConsulTestBase {

  @Test
  public void kvSet() {
    TxnRequest request = new TxnRequest()
      .addOperation(new TxnKV().setKey("foo/bar/t1").setValue("val1").setType(TxnKVType.SET))
      .addOperation(new TxnKV().setKey("foo/bar/t2").setValue("val2").setType(TxnKVType.SET));
    TxnResponse response = getAsync(h -> writeClient.transaction(request, h));
    assertEquals(0, response.getErrorsSize());
    assertEquals(2, response.getResultsSize());
    List<String> entries = getEntries("foo/bar/t");
    assertTrue(entries.contains("foo/bar/t1/val1"));
    assertTrue(entries.contains("foo/bar/t2/val2"));
    runAsync(h -> writeClient.deleteValues("foo/bar/t", h));
  }

  @Test
  public void kvCas() {
    long idx1 = createKV("foo/bar1", "value1");
    long idx2 = createKV("foo/bar2", "value2");

    TxnRequest req1 = new TxnRequest()
      .addOperation(new TxnKV().setKey("foo/bar1").setValue("newVal1").setIndex(idx1).setType(TxnKVType.CAS))
      .addOperation(new TxnKV().setKey("foo/bar2").setValue("newVal2").setIndex(idx2 - 1).setType(TxnKVType.CAS));
    TxnResponse resp1 = getAsync(h -> writeClient.transaction(req1, h));
    assertEquals(1, resp1.getErrorsSize());
    assertEquals(1, resp1.getErrors().get(0).getOpIndex());
    assertEquals(0, resp1.getResultsSize());

    TxnRequest req2 = new TxnRequest()
      .addOperation(new TxnKV().setKey("foo/bar1").setValue("newVal1").setIndex(idx1).setType(TxnKVType.CAS))
      .addOperation(new TxnKV().setKey("foo/bar2").setValue("newVal2").setIndex(idx2).setType(TxnKVType.CAS));
    TxnResponse resp2 = getAsync(h -> writeClient.transaction(req2, h));
    assertEquals(0, resp2.getErrorsSize());
    assertEquals(2, resp2.getResultsSize());
    List<String> entries = getEntries("foo/bar");
    assertTrue(entries.contains("foo/bar1/newVal1"));
    assertTrue(entries.contains("foo/bar2/newVal2"));
    runAsync(h -> writeClient.deleteValues("foo/bar", h));
  }

  private List<String> getEntries(String prefix) {
    KeyValueList list = getAsync(h -> readClient.getValues(prefix, h));
    return list.getList().stream()
      .map(kv -> kv.getKey() + "/" + kv.getValue()).collect(Collectors.toList());
  }

  private long createKV(String key, String value) {
    assertTrue(getAsync(h -> writeClient.putValue(key, value, h)));
    KeyValue pair = getAsync(h -> readClient.getValue(key, h));
    return pair.getModifyIndex();
  }
}

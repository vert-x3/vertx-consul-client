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
    KeyValueList list = getAsync(h -> readClient.getValues("foo/bar/t", h));
    assertEquals(2, list.getList().size());
    List<String> entries = list.getList().stream().map(kv -> kv.getKey() + "/" + kv.getValue()).collect(Collectors.toList());
    assertTrue(entries.contains("foo/bar/t1/val1"));
    assertTrue(entries.contains("foo/bar/t2/val2"));
    runAsync(h -> writeClient.deleteValues("foo/bar/t", h));
  }
}

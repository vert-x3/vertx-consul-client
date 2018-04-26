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

import io.vertx.core.Handler;
import io.vertx.ext.consul.*;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class Transactions extends ConsulTestBase {

  @Test
  public void kvSet(TestContext tc) {
    TxnRequest request = new TxnRequest()
      .addOperation(new TxnKVOperation().setKey("foo/bar/t1").setValue("val1").setType(TxnKVVerb.SET))
      .addOperation(new TxnKVOperation().setKey("foo/bar/t2").setValue("val2").setType(TxnKVVerb.SET));
    ctx.writeClient().transaction(request, tc.asyncAssertSuccess(response -> {
      tc.assertEquals(0, response.getErrorsSize());
      tc.assertEquals(2, response.getResultsSize());
      List<String> keys = getKeys(response);
      tc.assertTrue(keys.contains("foo/bar/t1"));
      tc.assertTrue(keys.contains("foo/bar/t2"));
      getEntries(tc, "foo/bar/t", entries -> {
        tc.assertTrue(entries.contains("foo/bar/t1/val1"));
        tc.assertTrue(entries.contains("foo/bar/t2/val2"));
        ctx.writeClient().deleteValues("foo/bar/t", tc.asyncAssertSuccess());
      });
    }));
  }

  @Test
  public void kvCas(TestContext tc) {
    createKV(tc, "foo/bar1", "value1", idx1 -> {
      createKV(tc, "foo/bar2", "value2", idx2 -> {
        TxnRequest req1 = new TxnRequest()
          .addOperation(new TxnKVOperation().setKey("foo/bar1").setValue("newVal1").setIndex(idx1).setType(TxnKVVerb.CAS))
          .addOperation(new TxnKVOperation().setKey("foo/bar2").setValue("newVal2").setIndex(idx2 - 1).setType(TxnKVVerb.CAS));
        ctx.writeClient().transaction(req1, tc.asyncAssertSuccess(resp1 -> {
          tc.assertEquals(1, resp1.getErrorsSize());
          tc.assertEquals(1, resp1.getErrors().get(0).getOpIndex());
          tc.assertEquals(0, resp1.getResultsSize());
          TxnRequest req2 = new TxnRequest()
            .addOperation(new TxnKVOperation().setKey("foo/bar1").setValue("newVal1").setIndex(idx1).setType(TxnKVVerb.CAS))
            .addOperation(new TxnKVOperation().setKey("foo/bar2").setValue("newVal2").setIndex(idx2).setType(TxnKVVerb.CAS));
          ctx.writeClient().transaction(req2, tc.asyncAssertSuccess(resp2 -> {
            tc.assertEquals(0, resp2.getErrorsSize());
            tc.assertEquals(2, resp2.getResultsSize());
            List<String> keys = getKeys(resp2);
            tc.assertTrue(keys.contains("foo/bar1"));
            tc.assertTrue(keys.contains("foo/bar2"));
            getEntries(tc, "foo/bar", entries -> {
              tc.assertTrue(entries.contains("foo/bar1/newVal1"));
              tc.assertTrue(entries.contains("foo/bar2/newVal2"));
              ctx.writeClient().deleteValues("foo/bar", tc.asyncAssertSuccess());
            });
          }));
        }));
      });
    });
  }

  private void getEntries(TestContext tc, String prefix, Handler<List<String>> resultHandler) {
    ctx.readClient().getValues(prefix, tc.asyncAssertSuccess(list -> {
      resultHandler.handle(list.getList().stream()
        .map(kv -> kv.getKey() + "/" + kv.getValue()).collect(Collectors.toList()));
    }));
  }

  private List<String> getKeys(TxnResponse resp) {
    return resp.getResults().stream()
      .map(kv -> ((KeyValue) kv).getKey()).collect(Collectors.toList());
  }

  private void createKV(TestContext tc, String key, String value, Handler<Long> resultHandler) {
    ctx.writeClient().putValue(key, value, tc.asyncAssertSuccess(b -> {
      tc.assertTrue(b);
      ctx.readClient().getValue(key, tc.asyncAssertSuccess(pair -> {
        resultHandler.handle(pair.getModifyIndex());
      }));
    }));
  }
}

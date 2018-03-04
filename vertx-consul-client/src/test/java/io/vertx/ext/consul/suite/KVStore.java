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
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import static io.vertx.test.core.TestUtils.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class KVStore extends ConsulTestBase {

  @Test
  public void handleExceptions() {
    CountDownLatch latch = new CountDownLatch(2);
    String unknownKey = randomFooBarAlpha();
    ctx.readClient().getValue(unknownKey, kv -> {
      latch.countDown();
      throw new RuntimeException();
    });
    boolean zero = true;
    try {
      zero = latch.await(2, TimeUnit.SECONDS);
    } catch (Exception ignore) {
    }
    assertFalse(zero);
  }

  @Test
  public void readClientCantWriteOneValue(TestContext tc) {
    ctx.readClient().putValue("foo/bar1", "value1", tc.asyncAssertFailure());
  }

  @Test
  public void readClientEmptyValue(TestContext tc) {
    String key = randomFooBarAlpha();
    ctx.writeClient()
      .putValue(key, "", tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        ctx.readClient().getValue(key, tc.asyncAssertSuccess(pair -> {
          tc.assertEquals(key, pair.getKey());
          tc.assertEquals("", pair.getValue());
          ctx.writeClient().deleteValue(key, tc.asyncAssertSuccess());
        }));
      }));
  }

  @Test
  public void readClientCanReadOneValue(TestContext tc) {
    String key = randomFooBarUnicode();
    String value = randomUnicodeString(10);
    ctx.writeClient()
      .putValue(key, value, tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        ctx.readClient().getValue(key, tc.asyncAssertSuccess(pair -> {
          tc.assertEquals(key, pair.getKey());
          tc.assertEquals(value, pair.getValue());
          ctx.writeClient().deleteValue(key, tc.asyncAssertSuccess());
        }));
      }));
  }

  @Test
  public void keyNotFound(TestContext tc) {
    String key = randomFooBarUnicode();
    ctx.readClient()
      .getValue(key, tc.asyncAssertSuccess(kv -> {
        tc.assertFalse(kv.isPresent());
      }));
  }

  @Test
  public void keysNotFound(TestContext tc) {
    String key = randomFooBarUnicode();
    ctx.readClient()
      .getValues(key, tc.asyncAssertSuccess(list -> {
        tc.assertFalse(list.isPresent());
      }));
  }

  @Test
  public void writeClientHaveFullAccessToOneValue(TestContext tc) {
    String key = randomFooBarAlpha();
    String value = randomAlphaString(10);
    KeyValueOptions opts = new KeyValueOptions().setFlags(randomLong());
    ctx.writeClient()
      .putValueWithOptions(key, value, opts, tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        ctx.readClient().getValue(key, tc.asyncAssertSuccess(pair -> {
          tc.assertEquals(key, pair.getKey());
          tc.assertEquals(value, pair.getValue());
          assertEquals(opts.getFlags(), pair.getFlags());
          ctx.writeClient().deleteValue(key, tc.asyncAssertSuccess());
        }));
      }));
  }

  @Test
  public void readClientCanReadValues(TestContext tc) {
    valuesAccess(tc, ctx.readClient());
  }

  @Test
  public void writeClientHaveFullAccessToValues(TestContext tc) {
    valuesAccess(tc, ctx.writeClient());
  }

  private void valuesAccess(TestContext tc, ConsulClient accessClient) {
    String key1 = randomFooBarAlpha();
    String value1 = randomAlphaString(10);
    String key2 = randomFooBarAlpha();
    String value2 = randomAlphaString(10);
    ctx.writeClient().putValue(key1, value1, tc.asyncAssertSuccess(b1 -> {
      tc.assertTrue(b1);
      ctx.writeClient().putValue(key2, value2, tc.asyncAssertSuccess(b2 -> {
        tc.assertTrue(b2);
        accessClient.getValues("foo/bar", tc.asyncAssertSuccess(kvList -> {
          List<KeyValue> list = kvList.getList();
          tc.assertEquals(list.size(), 2);
          tc.assertTrue(list.stream()
            .filter(kv -> kv.getKey().equals(key1) && kv.getValue().equals(value1))
            .count() == 1);
          tc.assertTrue(list.stream()
            .filter(kv -> kv.getKey().equals(key2) && kv.getValue().equals(value2))
            .count() == 1);
          ctx.writeClient().deleteValues("foo/bar", tc.asyncAssertSuccess());
        }));
      }));
    }));
  }

  @Test
  public void canSetAllFlags(TestContext tc) {
    String key = randomFooBarAlpha();
    String value = randomAlphaString(10);
    KeyValueOptions opts = new KeyValueOptions().setFlags(-1);
    ctx.writeClient()
      .putValueWithOptions(key, value, opts, tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        ctx.readClient().getValue(key, tc.asyncAssertSuccess(pair -> {
          tc.assertEquals(key, pair.getKey());
          tc.assertEquals(value, pair.getValue());
          assertEquals(opts.getFlags(), pair.getFlags());
          ctx.writeClient().deleteValue(key, tc.asyncAssertSuccess());
        }));
      }));
  }

  @Test
  public void checkAndSet(TestContext tc) {
    String key = randomFooBarAlpha();
    ctx.writeClient()
      .putValue(key, randomAlphaString(10), tc.asyncAssertSuccess(b1 -> {
        tc.assertTrue(b1);
        ctx.readClient().getValue(key, tc.asyncAssertSuccess(pair1 -> {
          ctx.writeClient().putValue(key, randomAlphaString(10), tc.asyncAssertSuccess(b2 -> {
            tc.assertTrue(b2);
            ctx.readClient().getValue(key, tc.asyncAssertSuccess(pair2 -> {
              tc.assertTrue(pair1.getModifyIndex() < pair2.getModifyIndex());
              ctx.writeClient().putValueWithOptions(key, randomAlphaString(10), new KeyValueOptions().setCasIndex(pair1.getModifyIndex()), tc.asyncAssertSuccess(b3 -> {
                tc.assertFalse(b3);
                ctx.writeClient().putValueWithOptions(key, randomAlphaString(10), new KeyValueOptions().setCasIndex(pair2.getModifyIndex()), tc.asyncAssertSuccess(b4 -> {
                  tc.assertTrue(b4);
                  ctx.writeClient().deleteValue(key, tc.asyncAssertSuccess());
                }));
              }));
            }));
          }));
        }));
      }));
  }

  @Test
  public void clientCantDeleteUnknownKey(TestContext tc) {
    ctx.readClient().deleteValue("unknown", tc.asyncAssertFailure());
  }

  @Test
  public void canGetValueBlocking(TestContext tc) {
    blockingQuery(tc, (key, h) -> ctx.readClient().getValue(key, tc.asyncAssertSuccess(list -> h.handle(list.getModifyIndex()))));
  }

  @Test
  public void canGetValuesBlocking(TestContext tc) {
    blockingQuery(tc, (key, h) -> ctx.readClient().getValues(key, tc.asyncAssertSuccess(list -> h.handle(list.getIndex()))));
  }

  @Test
  public void canGetKeysList(TestContext tc) {
    String keyPrefix = randomFooBarAlpha();
    String key = keyPrefix + randomAlphaString(10);
    ctx.writeClient()
      .putValue(key, randomAlphaString(10), tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        ctx.readClient().getKeys(keyPrefix, tc.asyncAssertSuccess(list -> {
          tc.assertTrue(list.contains(key));
          ctx.writeClient().deleteValues(keyPrefix, tc.asyncAssertSuccess());
        }));
      }));
  }

  private void blockingQuery(TestContext tc, BiConsumer<String, Handler<Long>> indexSupplier) {
    String key = randomFooBarAlpha();
    String value = randomAlphaString(10);
    ctx.writeClient()
      .putValue(key, randomAlphaString(10), tc.asyncAssertSuccess(b1 -> {
        tc.assertTrue(b1);
        indexSupplier.accept(key, consulIndex -> {
          Async async = tc.async(2);
          vertx.setTimer(TimeUnit.SECONDS.toMillis(2), l -> {
            ctx.writeClient().putValue(key, value, tc.asyncAssertSuccess(b2 -> {
              tc.assertTrue(b2);
              ctx.readClient().getValueWithOptions(key, new BlockingQueryOptions().setIndex(consulIndex), tc.asyncAssertSuccess(kv -> {
                tc.assertTrue(kv.getModifyIndex() > consulIndex);
                tc.assertEquals(kv.getValue(), value);
                async.countDown();
              }));
              ctx.readClient().getValuesWithOptions("foo/bar", new BlockingQueryOptions().setIndex(consulIndex), tc.asyncAssertSuccess(kv -> {
                tc.assertTrue(kv.getIndex() > consulIndex);
                tc.assertTrue(kv.getList().size() == 1);
                async.countDown();
              }));
            }));
          });
          async.handler(v -> ctx.writeClient().deleteValue(key, tc.asyncAssertSuccess()));
        });
      }));
  }
}

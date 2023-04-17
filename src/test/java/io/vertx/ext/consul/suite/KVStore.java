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
    readClient.getValue(unknownKey).onComplete(kv -> {
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
    readClient.putValue("foo/bar1", "value1").onComplete(tc.asyncAssertFailure());
  }

  @Test
  public void readClientEmptyValue(TestContext tc) {
    String key = randomFooBarAlpha();
    writeClient
      .putValue(key, "").onComplete(tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        readClient.getValue(key).onComplete(tc.asyncAssertSuccess(pair -> {
          tc.assertEquals(key, pair.getKey());
          tc.assertEquals("", pair.getValue());
          writeClient.deleteValue(key).onComplete(tc.asyncAssertSuccess());
        }));
      }));
  }

  @Test
  public void readClientCanReadOneValue(TestContext tc) {
    String key = randomFooBarUnicode();
    String value = randomUnicodeString(10);
    writeClient
      .putValue(key, value).onComplete(tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        readClient.getValue(key).onComplete(tc.asyncAssertSuccess(pair -> {
          tc.assertEquals(key, pair.getKey());
          tc.assertEquals(value, pair.getValue());
          writeClient.deleteValue(key).onComplete(tc.asyncAssertSuccess());
        }));
      }));
  }

  @Test
  public void keyNotFound(TestContext tc) {
    String key = randomFooBarUnicode();
    readClient
      .getValue(key).onComplete(tc.asyncAssertSuccess(kv -> {
        tc.assertFalse(kv.isPresent());
      }));
  }

  @Test
  public void keysNotFound(TestContext tc) {
    String key = randomFooBarUnicode();
    readClient
      .getValues(key).onComplete(tc.asyncAssertSuccess(list -> {
        tc.assertFalse(list.isPresent());
      }));
  }

  @Test
  public void writeClientHaveFullAccessToOneValue(TestContext tc) {
    String key = randomFooBarAlpha();
    String value = randomAlphaString(10);
    KeyValueOptions opts = new KeyValueOptions().setFlags(randomLong());
    writeClient
      .putValueWithOptions(key, value, opts).onComplete(tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        readClient.getValue(key).onComplete(tc.asyncAssertSuccess(pair -> {
          tc.assertEquals(key, pair.getKey());
          tc.assertEquals(value, pair.getValue());
          assertEquals(opts.getFlags(), pair.getFlags());
          writeClient.deleteValue(key).onComplete(tc.asyncAssertSuccess());
        }));
      }));
  }

  @Test
  public void readClientCanReadValues(TestContext tc) {
    valuesAccess(tc, readClient);
  }

  @Test
  public void writeClientHaveFullAccessToValues(TestContext tc) {
    valuesAccess(tc, writeClient);
  }

  private void valuesAccess(TestContext tc, ConsulClient accessClient) {
    String key1 = randomFooBarAlpha();
    String value1 = randomAlphaString(10);
    String key2 = randomFooBarAlpha();
    String value2 = randomAlphaString(10);
    writeClient.putValue(key1, value1).onComplete(tc.asyncAssertSuccess(b1 -> {
      tc.assertTrue(b1);
      writeClient.putValue(key2, value2).onComplete(tc.asyncAssertSuccess(b2 -> {
        tc.assertTrue(b2);
        accessClient.getValues("foo/bar").onComplete(tc.asyncAssertSuccess(kvList -> {
          List<KeyValue> list = kvList.getList();
          tc.assertEquals(list.size(), 2);
          tc.assertTrue(list.stream()
            .filter(kv -> kv.getKey().equals(key1) && kv.getValue().equals(value1))
            .count() == 1);
          tc.assertTrue(list.stream()
            .filter(kv -> kv.getKey().equals(key2) && kv.getValue().equals(value2))
            .count() == 1);
          writeClient.deleteValues("foo/bar").onComplete(tc.asyncAssertSuccess());
        }));
      }));
    }));
  }

  @Test
  public void canSetAllFlags(TestContext tc) {
    String key = randomFooBarAlpha();
    String value = randomAlphaString(10);
    KeyValueOptions opts = new KeyValueOptions().setFlags(-1);
    writeClient
      .putValueWithOptions(key, value, opts).onComplete(tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        readClient.getValue(key).onComplete(tc.asyncAssertSuccess(pair -> {
          tc.assertEquals(key, pair.getKey());
          tc.assertEquals(value, pair.getValue());
          assertEquals(opts.getFlags(), pair.getFlags());
          writeClient.deleteValue(key).onComplete(tc.asyncAssertSuccess());
        }));
      }));
  }

  @Test
  public void checkAndSet(TestContext tc) {
    String key = randomFooBarAlpha();
    writeClient
      .putValue(key, randomAlphaString(10)).onComplete(tc.asyncAssertSuccess(b1 -> {
        tc.assertTrue(b1);
        readClient.getValue(key).onComplete(tc.asyncAssertSuccess(pair1 -> {
          writeClient.putValue(key, randomAlphaString(10)).onComplete(tc.asyncAssertSuccess(b2 -> {
            tc.assertTrue(b2);
            readClient.getValue(key).onComplete(tc.asyncAssertSuccess(pair2 -> {
              tc.assertTrue(pair1.getModifyIndex() < pair2.getModifyIndex());
              writeClient.putValueWithOptions(key, randomAlphaString(10), new KeyValueOptions().setCasIndex(pair1.getModifyIndex())).onComplete(tc.asyncAssertSuccess(b3 -> {
                tc.assertFalse(b3);
                writeClient.putValueWithOptions(key, randomAlphaString(10), new KeyValueOptions().setCasIndex(pair2.getModifyIndex())).onComplete(tc.asyncAssertSuccess(b4 -> {
                  tc.assertTrue(b4);
                  writeClient.deleteValue(key).onComplete(tc.asyncAssertSuccess());
                }));
              }));
            }));
          }));
        }));
      }));
  }

  @Test
  public void clientCantDeleteUnknownKey(TestContext tc) {
    readClient.deleteValue("unknown").onComplete(tc.asyncAssertFailure());
  }

  @Test
  public void canGetValueBlocking(TestContext tc) {
    blockingQuery(tc, (key, h) -> readClient.getValue(key).onComplete(tc.asyncAssertSuccess(list -> h.handle(list.getModifyIndex()))));
  }

  @Test
  public void canGetValuesBlocking(TestContext tc) {
    blockingQuery(tc, (key, h) -> readClient.getValues(key).onComplete(tc.asyncAssertSuccess(list -> h.handle(list.getIndex()))));
  }

  @Test
  public void canGetKeysList(TestContext tc) {
    String keyPrefix = randomFooBarAlpha();
    String key = keyPrefix + randomAlphaString(10);
    writeClient
      .putValue(key, randomAlphaString(10)).onComplete(tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        readClient.getKeys(keyPrefix).onComplete(tc.asyncAssertSuccess(list -> {
          tc.assertTrue(list.contains(key));
          writeClient.deleteValues(keyPrefix).onComplete(tc.asyncAssertSuccess());
        }));
      }));
  }

  private void blockingQuery(TestContext tc, BiConsumer<String, Handler<Long>> indexSupplier) {
    String key = randomFooBarAlpha();
    String value = randomAlphaString(10);
    writeClient
      .putValue(key, randomAlphaString(10)).onComplete(tc.asyncAssertSuccess(b1 -> {
        tc.assertTrue(b1);
        indexSupplier.accept(key, consulIndex -> {
          Async async = tc.async(2);
          vertx.setTimer(TimeUnit.SECONDS.toMillis(2), l -> {
            writeClient.putValue(key, value).onComplete(tc.asyncAssertSuccess(b2 -> {
              tc.assertTrue(b2);
              readClient.getValueWithOptions(key, new BlockingQueryOptions().setIndex(consulIndex)).onComplete(tc.asyncAssertSuccess(kv -> {
                tc.assertTrue(kv.getModifyIndex() > consulIndex);
                tc.assertEquals(kv.getValue(), value);
                async.countDown();
              }));
              readClient.getValuesWithOptions("foo/bar", new BlockingQueryOptions().setIndex(consulIndex)).onComplete(tc.asyncAssertSuccess(kv -> {
                tc.assertTrue(kv.getIndex() > consulIndex);
                tc.assertTrue(kv.getList().size() == 1);
                async.countDown();
              }));
            }));
          });
          async.handler(v -> writeClient.deleteValue(key).onComplete(tc.asyncAssertSuccess()));
        });
      }));
  }
}

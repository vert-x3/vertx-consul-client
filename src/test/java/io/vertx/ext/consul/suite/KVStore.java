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
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Single;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
    Async async = tc.async();
    ctx.rxReadClient().rxPutValue("foo/bar1", "value1")
      .subscribe(o -> tc.fail(), t -> async.complete());
  }

  @Test
  public void readClientEmptyValue(TestContext tc) {
    Async async = tc.async();
    String key = randomFooBarAlpha();
    ctx.rxWriteClient()
      .rxPutValue(key, "")
      .map(check(tc::assertTrue))
      .flatMap(b -> ctx.rxReadClient().rxGetValue(key))
      .map(check(pair -> tc.assertEquals(key, pair.getKey())))
      .map(check(pair -> tc.assertEquals("", pair.getValue())))
      .flatMap(pair -> ctx.rxWriteClient().rxDeleteValue(key))
      .subscribe(o -> async.complete(), tc::fail);
  }

  @Test
  public void readClientCanReadOneValue(TestContext tc) {
    Async async = tc.async();
    String key = randomFooBarUnicode();
    String value = randomUnicodeString(10);
    ctx.rxWriteClient()
      .rxPutValue(key, value)
      .map(check(tc::assertTrue))
      .flatMap(b -> ctx.rxReadClient().rxGetValue(key))
      .map(check(pair -> tc.assertEquals(key, pair.getKey())))
      .map(check(pair -> tc.assertEquals(value, pair.getValue())))
      .flatMap(pair -> ctx.rxWriteClient().rxDeleteValue(key))
      .subscribe(o -> async.complete(), tc::fail);
  }

  @Test
  public void keyNotFound(TestContext tc) {
    Async async = tc.async();
    String key = randomFooBarUnicode();
    ctx.rxReadClient()
      .rxGetValue(key)
      .map(check(kv -> tc.assertFalse(kv.isPresent())))
      .subscribe(o -> async.complete(), tc::fail);
  }

  @Test
  public void keysNotFound(TestContext tc) {
    Async async = tc.async();
    String key = randomFooBarUnicode();
    ctx.rxReadClient()
      .rxGetValues(key)
      .map(check(list -> tc.assertFalse(list.isPresent())))
      .subscribe(o -> async.complete(), tc::fail);
  }

  @Test
  public void writeClientHaveFullAccessToOneValue(TestContext tc) {
    Async async = tc.async();
    String key = randomFooBarAlpha();
    String value = randomAlphaString(10);
    KeyValueOptions opts = new KeyValueOptions().setFlags(randomLong());
    ctx.rxWriteClient()
      .rxPutValueWithOptions(key, value, opts)
      .map(check(tc::assertTrue))
      .flatMap(b -> ctx.rxReadClient().rxGetValue(key))
      .map(check(pair -> tc.assertEquals(key, pair.getKey())))
      .map(check(pair -> tc.assertEquals(value, pair.getValue())))
      .map(check(pair -> assertEquals(opts.getFlags(), pair.getFlags())))
      .flatMap(pair -> ctx.rxWriteClient().rxDeleteValue(key))
      .subscribe(o -> async.complete(), tc::fail);
  }

  @Test
  public void readClientCanReadValues(TestContext tc) {
    valuesAccess(tc, ctx.rxReadClient());
  }

  @Test
  public void writeClientHaveFullAccessToValues(TestContext tc) {
    valuesAccess(tc, ctx.rxWriteClient());
  }

  private void valuesAccess(TestContext tc, io.vertx.rxjava.ext.consul.ConsulClient accessClient) {
    Async async = tc.async();
    String key1 = randomFooBarAlpha();
    String value1 = randomAlphaString(10);
    String key2 = randomFooBarAlpha();
    String value2 = randomAlphaString(10);
    Single.concat(
      ctx.rxWriteClient().rxPutValue(key1, value1),
      ctx.rxWriteClient().rxPutValue(key2, value2))
      .reduce(true, (b1, b2) -> b1 & b2).toSingle()
      .map(check(tc::assertTrue))
      .flatMap(b -> accessClient.rxGetValues("foo/bar"))
      .map(KeyValueList::getList)
      .map(check(list -> tc.assertEquals(list.size(), 2)))
      .map(check(list -> tc.assertTrue(list.stream()
        .filter(kv -> kv.getKey().equals(key1) && kv.getValue().equals(value1))
        .count() == 1)))
      .map(check(list -> tc.assertTrue(list.stream()
        .filter(kv -> kv.getKey().equals(key2) && kv.getValue().equals(value2))
        .count() == 1)))
      .flatMap(list -> ctx.rxWriteClient().rxDeleteValues("foo/bar"))
      .subscribe(o -> async.complete(), tc::fail);
  }

  @Test
  public void canSetAllFlags(TestContext tc) {
    Async async = tc.async();
    String key = randomFooBarAlpha();
    String value = randomAlphaString(10);
    KeyValueOptions opts = new KeyValueOptions().setFlags(-1);
    ctx.rxWriteClient()
      .rxPutValueWithOptions(key, value, opts)
      .map(check(tc::assertTrue))
      .flatMap(b -> ctx.rxReadClient().rxGetValue(key))
      .map(check(pair -> tc.assertEquals(key, pair.getKey())))
      .map(check(pair -> tc.assertEquals(value, pair.getValue())))
      .map(check(pair -> assertEquals(opts.getFlags(), pair.getFlags())))
      .flatMap(pair -> ctx.rxWriteClient().rxDeleteValue(key))
      .subscribe(o -> async.complete(), tc::fail);
  }

  @Test
  public void checkAndSet(TestContext tc) {
    Async async = tc.async();
    String key = randomFooBarAlpha();
    ctx.rxWriteClient()
      .rxPutValue(key, randomAlphaString(10))
      .map(check(tc::assertTrue))
      .flatMap(b1 -> ctx.rxReadClient().rxGetValue(key))
      .flatMap(pair1 -> ctx.rxWriteClient().rxPutValue(key, randomAlphaString(10))
        .map(check(tc::assertTrue))
        .flatMap(b2 -> ctx.rxReadClient().rxGetValue(key))
        .map(check(pair2 -> tc.assertTrue(pair1.getModifyIndex() < pair2.getModifyIndex())))
        .flatMap(pair2 -> Single.concat(
          ctx.rxWriteClient().rxPutValueWithOptions(key, randomAlphaString(10), new KeyValueOptions().setCasIndex(pair1.getModifyIndex())).map(b -> !b),
          ctx.rxWriteClient().rxPutValueWithOptions(key, randomAlphaString(10), new KeyValueOptions().setCasIndex(pair2.getModifyIndex())))
          .reduce(true, (b1, b2) -> b1 & b2).toSingle())
      )
      .flatMap(pair -> ctx.rxWriteClient().rxDeleteValue(key))
      .subscribe(o -> async.complete(), tc::fail);
  }

  @Test
  public void clientCantDeleteUnknownKey(TestContext tc) {
    Async async = tc.async();
    ctx.rxReadClient().rxDeleteValue("unknown")
      .subscribe(o -> tc.fail(), t -> async.complete());
  }

  @Test
  public void canGetValueBlocking(TestContext tc) {
    blockingQuery(tc, key -> ctx.rxReadClient().rxGetValue(key).map(KeyValue::getModifyIndex));
  }

  @Test
  public void canGetValuesBlocking(TestContext tc) {
    blockingQuery(tc, key -> ctx.rxReadClient().rxGetValues(key).map(KeyValueList::getIndex));
  }

  @Test
  public void canGetKeysList(TestContext tc) {
    Async async = tc.async();
    String keyPrefix = randomFooBarAlpha();
    String key = keyPrefix + randomAlphaString(10);
    ctx.rxWriteClient()
      .rxPutValue(key, randomAlphaString(10))
      .map(check(tc::assertTrue))
      .flatMap(b -> ctx.rxReadClient().rxGetKeys(keyPrefix))
      .map(check(list -> tc.assertTrue(list.contains(key))))
      .flatMap(list -> ctx.rxWriteClient().rxDeleteValues(keyPrefix))
      .subscribe(o -> async.complete(), tc::fail);
  }

  private void blockingQuery(TestContext tc, Function<String, Single<Long>> indexSupplier) {
    Async async = tc.async();
    String key = randomFooBarAlpha();
    String value = randomAlphaString(10);
    ctx.rxWriteClient()
      .rxPutValue(key, randomAlphaString(10))
      .map(check(tc::assertTrue))
      .flatMap(b -> indexSupplier.apply(key))
      .flatMap(consulIndex -> Single.concat(
        Single.just(ctx.rxWriteClient()).delay(2, TimeUnit.SECONDS)
          .flatMap(c -> c.rxPutValue(key, value).map(check(tc::assertTrue))),
        ctx.rxReadClient().rxGetValueWithOptions(key, new BlockingQueryOptions().setIndex(consulIndex))
          .map(check(kv -> tc.assertTrue(kv.getModifyIndex() > consulIndex)))
          .map(check(kv -> tc.assertEquals(kv.getValue(), value))),
        ctx.rxReadClient().rxGetValuesWithOptions("foo/bar", new BlockingQueryOptions().setIndex(consulIndex))
          .map(check(list -> tc.assertTrue(list.getIndex() > consulIndex)))
          .map(check(list -> tc.assertTrue(list.getList().size() == 1))))
        .reduce(null, (ignore1, ignore2) -> null).toSingle())
      .flatMap(pair -> ctx.rxWriteClient().rxDeleteValue(key))
      .subscribe(o -> async.complete(), tc::fail);
  }
}

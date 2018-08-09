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

import io.vertx.core.Future;
import io.vertx.ext.consul.*;
import io.vertx.ext.consul.common.StateConsumer;
import io.vertx.ext.consul.dc.ConsulAgent;
import io.vertx.ext.consul.impl.WatchKeyPrefixCnt;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;
import static io.vertx.test.core.TestUtils.randomAlphaString;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Watches extends ConsulTestBase {

  private static final String EMPTY_MESSAGE = randomAlphaString(10);
  private static final String CONNECTION_REFUSED = "Connection refused";

  @Test
  public void throwStartStart() {
    Watch<KeyValue> watch = Watch.key(randomAlphaString(10), vertx);
    try {
      watch.start().start();
    } catch (IllegalStateException e) {
      watch.stop();
      return;
    }
    fail();
  }

  @Test(expected = IllegalStateException.class)
  public void throwStop() {
    Watch<KeyValue> watch = Watch.key(randomAlphaString(10), vertx);
    watch.stop();
  }

  @Test(expected = IllegalStateException.class)
  public void throwStartStopStop() {
    Watch<KeyValue> watch = Watch.key(randomAlphaString(10), vertx);
    watch.start().stop();
    watch.stop();
  }

  @Test
  public void connectionRefused() throws InterruptedException {
    StateConsumer<Long> consumer = new StateConsumer<>();
    String key = ConsulContext.KEY_RW_PREFIX + randomAlphaString(10);
    long t0 = System.currentTimeMillis();

    Watch<KeyValue> watch = Watch.key(key, vertx, new ConsulClientOptions().setPort(Utils.getFreePort()))
      .setHandler(h -> {
        if (h.succeeded()) {
          fail();
        } else {
          assertTrue(h.failed());
          assertTrue(h.cause().getMessage().contains(CONNECTION_REFUSED));
          consumer.consume(System.currentTimeMillis() - t0);
        }
      })
      .start();

    for (int i = 0; i < 5; i++) {
      consumer.awaitAny();
    }
    consumer.check();
    watch.stop();

    List<Long> ticks = consumer.getConsumed();
    List<Long> diff;
    diff = diff(ticks); // parabolic
    diff = diff(diff);  // proportionality
    diff = diff(diff);  // constant (~2000)
    long zero = diff(diff).get(0);  // zero
    System.out.println("zero: " + zero);
    assertTrue(Math.abs(zero) < 1000);
  }

  private static List<Long> diff(List<Long> list) {
    List<Long> res = new ArrayList<>();
    for (int i = 1; i < list.size(); i++) {
      res.add(list.get(i) - list.get(i - 1));
    }
    return res;
  }

  @Test
  public void watchCreatedKey() throws InterruptedException {
    StateConsumer<String> consumer = new StateConsumer<>();
    String key = ConsulContext.KEY_RW_PREFIX + randomAlphaString(10);
    String v1 = randomAlphaString(10);
    String v2 = randomAlphaString(10);

    Watch<KeyValue> watch = Watch.key(key, vertx, ctx.readClientOptions())
      .setHandler(kv -> {
        if (kv.succeeded()) {
          consumer.consume(kv.nextResult().isPresent() ? kv.nextResult().getValue() : EMPTY_MESSAGE);
        } else {
          consumer.consume(kv.cause().getMessage());
        }
      })
      .start();

    consumer.await(EMPTY_MESSAGE);

    assertTrue(getAsync(h -> ctx.writeClient().putValue(key, v1, h)));
    consumer.await(v1);
    assertTrue(getAsync(h -> ctx.writeClient().putValue(key, v2, h)));
    consumer.await(v2);
    runAsync(h -> ctx.writeClient().deleteValue(key, h));
    consumer.await(EMPTY_MESSAGE);
    consumer.check();

    watch.stop();
  }

  @Test
  public void watchExistingKey() throws InterruptedException {
    StateConsumer<String> consumer = new StateConsumer<>();
    String key = ConsulContext.KEY_RW_PREFIX + randomAlphaString(10);
    String v1 = randomAlphaString(10);
    String v2 = randomAlphaString(10);

    assertTrue(getAsync(h -> ctx.writeClient().putValue(key, v1, h)));

    Watch<KeyValue> watch = Watch.key(key, vertx, ctx.readClientOptions())
      .setHandler(kv -> {
        if (kv.succeeded()) {
          consumer.consume(kv.nextResult().isPresent() ? kv.nextResult().getValue() : EMPTY_MESSAGE);
        } else {
          consumer.consume(kv.cause().getMessage());
        }
      })
      .start();

    consumer.await(v1);
    assertTrue(getAsync(h -> ctx.writeClient().putValue(key, v2, h)));
    consumer.await(v2);
    runAsync(h -> ctx.writeClient().deleteValue(key, h));
    consumer.await(EMPTY_MESSAGE);
    consumer.check();

    watch.stop();
  }

  @Test
  public void testKeyPrefix() throws InterruptedException {
    StateConsumer<String> consumer = new StateConsumer<>();
    String keyPrefix = ConsulContext.KEY_RW_PREFIX + randomAlphaString(10);
    String k1 = keyPrefix + randomAlphaString(10);
    String k2 = keyPrefix + randomAlphaString(10);
    String v1 = randomAlphaString(10);
    String v2 = randomAlphaString(10);

    assertTrue(getAsync(h -> ctx.writeClient().putValue(k1, v1, h)));

    Watch<KeyValueList> watch = Watch.keyPrefix(keyPrefix, vertx, ctx.readClientOptions())
      .setHandler(kv -> {
        if (kv.succeeded()) {
          if (kv.nextResult().isPresent()) {
            consumer.consume(kv.nextResult().getList().stream().map(KeyValue::getValue).sorted().collect(Collectors.joining("/")));
          } else {
            consumer.consume(EMPTY_MESSAGE);
          }
        } else {
          consumer.consume(kv.cause().getMessage());
        }
      })
      .start();

    consumer.await(v1);
    assertTrue(getAsync(h -> ctx.writeClient().putValue(k2, v2, h)));
    consumer.await(Stream.of(v1, v2).sorted().collect(Collectors.joining("/")));
    runAsync(h -> ctx.writeClient().deleteValues(keyPrefix, h));
    consumer.await(EMPTY_MESSAGE);
    consumer.check();

    watch.stop();
  }

  @Test
  public void iss54() {
    String keyPrefix = ConsulContext.KEY_RW_PREFIX + randomAlphaString(10);
    AtomicInteger eventCnt = new AtomicInteger();

    WatchKeyPrefixCnt watch = new WatchKeyPrefixCnt(keyPrefix, vertx, ctx.readClientOptions());
    watch.setHandler(kv -> eventCnt.incrementAndGet());
    watch.start();

    runAsync(h -> vertx.setTimer(1500, l -> h.handle(Future.succeededFuture())));

    assertEquals(1, eventCnt.get());
    assertTrue(watch.cnt() < 5);

    watch.stop();
  }

  @Test
  public void watchServices() throws InterruptedException {
    StateConsumer<String> consumer = new StateConsumer<>();

    ServiceOptions service = new ServiceOptions()
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10));

    Watch<ServiceList> watch = Watch.services(vertx, ctx.readClientOptions())
      .setHandler(list -> {
        if (list.succeeded()) {
          consumer.consume(list.nextResult().getList()
            .stream().map(Service::getName).filter(s -> s.equals(service.getName()))
            .findFirst().orElse(""));
        }
      })
      .start();

    consumer.await("");

    runAsync(h -> ctx.writeClient().registerService(service, h));
    consumer.await(service.getName());
    consumer.check();

    watch.stop();
    runAsync(h -> ctx.writeClient().deregisterService(service.getId(), h));
  }

  @Test
  public void watchService() throws InterruptedException {
    StateConsumer<String> consumer = new StateConsumer<>();

    ServiceOptions service = new ServiceOptions()
      .setCheckOptions(new CheckOptions()
        .setStatus(CheckStatus.PASSING)
        .setTtl("4s")
        .setName(randomAlphaString(10)))
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10));

    Watch<ServiceEntryList> watch = Watch.service(service.getName(), vertx, ctx.readClientOptions())
      .setHandler(list -> {
        if (list.succeeded()) {
          consumer.consume(list.nextResult().getList()
            .stream().filter(s -> s.getService().getName().equals(service.getName()))
            .map(e -> e.getService().getName() + "/" + e.getChecks().stream()
              .filter(c -> c.getId().equals("service:" + service.getId()))
              .map(c -> c.getStatus().name()).findFirst().orElse(""))
            .findFirst().orElse(""));
        }
      })
      .start();

    consumer.await("");

    runAsync(h -> ctx.writeClient().registerService(service, h));
    consumer.await(service.getName() + "/" + CheckStatus.PASSING.name());
    consumer.await(service.getName() + "/" + CheckStatus.CRITICAL.name());
    consumer.check();

    watch.stop();
    runAsync(h -> ctx.writeClient().deregisterService(service.getId(), h));
  }

  @Test
  public void watchEvents() throws InterruptedException {
    StateConsumer<String> consumer = new StateConsumer<>();
    String evName = randomAlphaString(10);
    String p1 = randomAlphaString(10);
    String p2 = randomAlphaString(10);

    Watch<EventList> watch = Watch.events(evName, vertx, ctx.readClientOptions())
      .setHandler(list -> {
        if (list.succeeded()) {
          consumer.consume(list.nextResult().getList()
            .stream().map(Event::getPayload).collect(Collectors.joining(",")));
        }
      })
      .start();

    consumer.await("");

    Utils.<Event>getAsync(h -> ctx.writeClient().fireEventWithOptions(evName, new EventOptions().setPayload(p1), h));
    Utils.<Event>getAsync(h -> ctx.writeClient().fireEventWithOptions(randomAlphaString(10), new EventOptions().setPayload(randomAlphaString(10)), h));
    Utils.<Event>getAsync(h -> ctx.writeClient().fireEventWithOptions(evName, new EventOptions().setPayload(p2), h));

    consumer.await(p1);
    consumer.await(p1 + "," + p2);
    consumer.check();

    watch.stop();
  }

  @Test
  public void watchNodes() throws InterruptedException {
    StateConsumer<String> consumer = new StateConsumer<>();
    String nodeName = randomAlphaString(10);

    Watch<NodeList> watch = Watch.nodes(vertx, ctx.readClientOptions())
      .setHandler(list -> {
        if (list.succeeded()) {
          consumer.consume(list.nextResult().getList()
            .stream().map(Node::getName).filter(s -> s.equals(nodeName))
            .findFirst().orElse(""));
        }
      })
      .start();

    consumer.await("");

    ConsulAgent attached = ctx.attachAgent(nodeName);
    ctx.detachAgent(attached);

    consumer.await(nodeName);
    consumer.await("");
    consumer.check();

    watch.stop();
  }
}

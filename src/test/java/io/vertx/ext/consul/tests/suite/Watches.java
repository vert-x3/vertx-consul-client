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
package io.vertx.ext.consul.tests.suite;

import io.vertx.core.Future;
import io.vertx.ext.consul.*;
import io.vertx.ext.consul.tests.impl.WatchKeyPrefixCnt;
import io.vertx.ext.consul.tests.instance.ConsulInstance;
import io.vertx.ext.consul.tests.ConsulTestBase;
import io.vertx.ext.consul.tests.Utils;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vertx.ext.consul.tests.Utils.getAsync;
import static io.vertx.ext.consul.tests.Utils.runAsync;
import static io.vertx.test.core.TestUtils.randomAlphaString;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class Watches extends ConsulTestBase {

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
  public void connectionRefused(TestContext tc) {
    String key = KEY_RW_PREFIX + randomAlphaString(10);
    long t0 = System.currentTimeMillis();
    Async async = tc.async(5);
    List<Long> ticks = new ArrayList<>();

    Watch<KeyValue> watch = Watch.key(key, vertx, new ConsulClientOptions().setPort(Utils.getFreePort()))
      .setHandler(h -> {
        if (h.succeeded()) {
          fail();
        } else {
          tc.assertTrue(h.failed());
          tc.assertTrue(h.cause().getMessage().contains(CONNECTION_REFUSED));
          ticks.add(System.currentTimeMillis() - t0);
          async.countDown();
        }
      })
      .start();

    async.await(TimeUnit.SECONDS.toMillis(45));
    tc.assertEquals(5, ticks.size());
    watch.stop();

    List<Long> diff;
    diff = diff(ticks); // parabolic
    diff = diff(diff);  // proportionality
    diff = diff(diff);  // constant (~2000)
    long zero = diff(diff).get(0);  // zero
    System.out.println("zero: " + zero);
    tc.assertTrue(Math.abs(zero) < 1000);
  }

  private static List<Long> diff(List<Long> list) {
    List<Long> res = new ArrayList<>();
    for (int i = 1; i < list.size(); i++) {
      res.add(list.get(i) - list.get(i - 1));
    }
    return res;
  }

  @Test
  public void clientWithTimeout(TestContext tc) {
    String key = KEY_RW_PREFIX + randomAlphaString(10);
    String v1 = randomAlphaString(10);
    String v2 = randomAlphaString(10);
    String v3 = randomAlphaString(10);

    ConsulClientOptions clientOptions = new ConsulClientOptions()
      .setHost(consul.getHost())
      .setPort(consul.getMappedPort(8500))
      .setSsl(false)
      .setTrustAll(true)
      .setVerifyHost(false)
      .setAclToken(dc.getMasterToken())
      .setKeepAlive(false)
      .setTimeout(5_000);
    Async latch = tc.async(1);
    Async latch2 = tc.async(2);
    Async latch3 = tc.async(3);
    Watch<KeyValue> watch = Watch.key(key, vertx, clientOptions)
      .setHandler(kv -> {
        if (kv.succeeded()) {
          KeyValue state = kv.nextResult();
          if (state.isPresent()) {
            System.out.println("State " + state.getValue());
            latch.countDown();
            latch2.countDown();
            latch3.countDown();
          }
        }
      })
      .start();

    System.out.println("Send update: " + v1);
    assertTrue(getAsync(() -> writeClient.putValue(key, v1)));
    latch.await(10_000);
    System.out.println("Send update: " + v2);
    assertTrue(getAsync(() -> writeClient.putValue(key, v2)));
    latch2.await(10_000);

    System.out.println("Waiting for more than the timeout value");
    Utils.sleep(vertx, 8_000);

    System.out.println("Send update: " + v3);
    assertTrue(getAsync(() -> writeClient.putValue(key, v3)));
    latch3.await(10_000);

    watch.stop();
  }

  @Test
  public void watchCreatedKey(TestContext tc) {
    String key = KEY_RW_PREFIX + randomAlphaString(10);
    String v1 = randomAlphaString(10);
    String v2 = randomAlphaString(10);

    Async emptyKey = tc.async(1);
    Async afterDelete = tc.async(2);
    Async firstValue = tc.async(1);
    Async secondValue = tc.async(2);

    AtomicReference<String> current = new AtomicReference<>("");

    Watch<KeyValue> watch = Watch.key(key, vertx, consul.consulClientOptions(consul.dc().readToken()))
      .setHandler(kv -> {
        if (kv.succeeded()) {
          if (kv.nextResult().isPresent()) {
            current.set(kv.nextResult().getValue());
            firstValue.countDown();
            secondValue.countDown();
          } else {
            emptyKey.countDown();
            afterDelete.countDown();
          }
        } else tc.fail(kv.cause().getMessage());
      })
      .start();

    emptyKey.await(500);

    tc.assertTrue(getAsync(() -> writeClient.putValue(key, v1)));
    firstValue.await(2000);
    tc.assertEquals(v1, current.get());

    tc.assertTrue(getAsync(() -> writeClient.putValue(key, v2)));
    secondValue.await(700);
    tc.assertEquals(v2, current.get());

    runAsync(() -> writeClient.deleteValue(key));
    afterDelete.await(500);

    watch.stop();
  }

  @Test
  public void watchExistingKey(TestContext tc) {
    String key = KEY_RW_PREFIX + randomAlphaString(10);
    String v1 = randomAlphaString(10);
    String v2 = randomAlphaString(10);

    Async firstValue = tc.async(1);
    Async secondValue = tc.async(2);
    Async emptyKey = tc.async(1);

    AtomicReference<String> current = new AtomicReference<>("");

    tc.assertTrue(getAsync(() -> writeClient.putValue(key, v1)));

    Watch<KeyValue> watch = Watch.key(key, vertx, consul.consulClientOptions(consul.dc().readToken()))
      .setHandler(kv -> {
        if (kv.succeeded()) {
          if (kv.nextResult().isPresent()) {
            current.set(kv.nextResult().getValue());
            firstValue.countDown();
            secondValue.countDown();
          } else emptyKey.countDown();
        }
      })
      .start();

    firstValue.await(500);
    tc.assertEquals(v1, current.get());

    tc.assertTrue(getAsync(() -> writeClient.putValue(key, v2)));
    secondValue.await(500);
    tc.assertEquals(v2, current.get());

    runAsync(() -> writeClient.deleteValue(key));
    emptyKey.await(500);

    watch.stop();
  }

  @Test
  public void testKeyPrefix(TestContext tc) {
    String keyPrefix = KEY_RW_PREFIX + randomAlphaString(10);
    String k1 = keyPrefix + randomAlphaString(10);
    String k2 = keyPrefix + randomAlphaString(10);
    String v1 = randomAlphaString(10);
    String v2 = randomAlphaString(10);
    Async firstKey = tc.async(1);
    Async secondKey = tc.async(2);
    Async emptyKey = tc.async(1);

    AtomicReference<String> current = new AtomicReference<>("");

    tc.assertTrue(getAsync(() -> writeClient.putValue(k1, v1)));

    Watch<KeyValueList> watch = Watch.keyPrefix(keyPrefix, vertx, consul.consulClientOptions(consul.dc().readToken()))
      .setHandler(kv -> {
        if (kv.succeeded()) {
          if (kv.nextResult().isPresent()) {
            current.set(kv.nextResult()
              .getList()
              .stream()
              .map(KeyValue::getValue)
              .sorted()
              .collect(Collectors.joining("/")));
            firstKey.countDown();
            secondKey.countDown();
          } else emptyKey.countDown();
        }
      })
      .start();

    firstKey.await(500);
    tc.assertEquals(v1, current.get());

    tc.assertTrue(getAsync(() -> writeClient.putValue(k2, v2)));
    secondKey.await(500);
    tc.assertEquals(Stream.of(v1, v2).sorted().collect(Collectors.joining("/")), current.get());

    runAsync(() -> writeClient.deleteValues(keyPrefix));
    emptyKey.countDown();

    watch.stop();
  }

  @Test
  public void iss54(TestContext tc) {
    String keyPrefix = KEY_RW_PREFIX + randomAlphaString(10);
    AtomicInteger eventCnt = new AtomicInteger();

    WatchKeyPrefixCnt watch = new WatchKeyPrefixCnt(
      keyPrefix,
      vertx,
      consul.consulClientOptions(consul.dc().readToken())
    );
    watch.setHandler(kv -> eventCnt.incrementAndGet());
    watch.start();

    runAsync(h -> vertx.setTimer(1500, l -> h.handle(Future.succeededFuture())));

    tc.assertEquals(1, eventCnt.get());
    tc.assertTrue(watch.cnt() < 5);

    watch.stop();
  }

  @Test
  public void iss70(TestContext tc) {
    String key = KEY_RW_PREFIX + randomAlphaString(10);
    String v1 = randomAlphaString(10);
    String v2 = randomAlphaString(10);

    tc.assertTrue(getAsync(() -> writeClient.putValue(key, v1)));

    Async succ = tc.async(1);
    AtomicInteger errs = new AtomicInteger();
    Watch<KeyValue> watch = Watch
      .key(
        key,
        vertx,
        new ConsulClientOptions(consul.consulClientOptions(consul
          .dc()
          .readToken())).setTimeout(TimeUnit.SECONDS.toMillis(10))
      )
      .setHandler(kv -> {
        if (kv.succeeded()) {
          KeyValue nextResult = kv.nextResult();
          if (nextResult.isPresent() && nextResult.getValue().equals(v2)) {
            succ.countDown();
          }
        } else {
          errs.incrementAndGet();
          System.out.println("got error: " + kv.cause());
        }
      })
      .start();

    vertx.setTimer(TimeUnit.SECONDS.toMillis(15), th -> {
      writeClient.putValue(key, v2);
    });

    succ.await(TimeUnit.SECONDS.toMillis(16));
    tc.assertEquals(errs.get(), 0);
    watch.stop();
  }

  @Test
  public void watchServices(TestContext tc) {
    Async empty = tc.async(1);
    Async serviceAwait = tc.async(1);

    ServiceOptions service = new ServiceOptions()
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10));

    Watch<ServiceList> watch = Watch.services(vertx, consul.consulClientOptions(consul.dc().readToken()))
      .setHandler(list -> {
        if (list.succeeded()) {
          Optional<String> resultService = list.nextResult().getList()
            .stream().map(Service::getName).filter(s -> s.equals(service.getName()))
            .findFirst();
          if (resultService.isPresent()) {
            tc.assertEquals(service.getName(), resultService.get());
            serviceAwait.countDown();
          } else empty.countDown();
        }
      })
      .start();

    empty.await(500);

    runAsync(() -> writeClient.registerService(service));
    serviceAwait.await(500);

    watch.stop();
    runAsync(() -> writeClient.deregisterService(service.getId()));
  }

  @Test
  public void watchService(TestContext tc) {
    ServiceOptions service = new ServiceOptions()
      .setCheckOptions(new CheckOptions()
        .setStatus(CheckStatus.PASSING)
        .setTtl("2s")
        .setName(randomAlphaString(10)))
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10));

    AtomicReference<String> current = new AtomicReference<>("");
    Async servicePassing = tc.async(1);
    Async serviceCritical = tc.async(2);
    Async empty = tc.async(1);

    Watch<ServiceEntryList> watch = Watch
      .service(service.getName(), vertx, consul.consulClientOptions(consul.dc().readToken()))
      .setHandler(list -> {
        if (list.succeeded()) {
          String value = list.nextResult().getList()
            .stream().filter(s -> s.getService().getName().equals(service.getName()))
            .map(e -> e.getService().getName() + "/" + e.getChecks().stream()
              .filter(c -> c.getId().equals("service:" + service.getId()))
              .map(c -> c.getStatus().name()).findFirst().orElse(""))
            .findFirst().orElse("");
          if (!value.isEmpty()) {
            current.set(value);
            servicePassing.countDown();
            serviceCritical.countDown();
          } else empty.countDown();
        }
      })
      .start();

    empty.await(500);

    runAsync(() -> writeClient.registerService(service));
    servicePassing.await(500);
    tc.assertEquals(service.getName() + "/" + CheckStatus.PASSING.name(), current.get());
    serviceCritical.await(2300);
    tc.assertEquals(service.getName() + "/" + CheckStatus.CRITICAL.name(), current.get());

    watch.stop();
    runAsync(() -> writeClient.deregisterService(service.getId()));
  }

  @Test
  public void watchEvents(TestContext tc) {
    String evName = randomAlphaString(10);
    String p1 = randomAlphaString(10);
    String p2 = randomAlphaString(10);
    Async empty = tc.async(1);
    Async firstEvent = tc.async(1);
    Async secondEvent = tc.async(2);
    AtomicReference<String> current = new AtomicReference<>("");

    Watch<EventList> watch = Watch.events(evName, vertx, consul.consulClientOptions(consul.dc().readToken()))
      .setHandler(list -> {
        if (list.succeeded()) {
          String val = list.nextResult().getList()
            .stream().map(Event::getPayload).collect(Collectors.joining(","));
          if (!val.isEmpty()) {
            current.set(val);
            firstEvent.countDown();
            secondEvent.countDown();
          } else empty.countDown();
        }
      })
      .start();

    empty.await(500);
    System.out.println("Empty events list");

    Utils.getAsync(() -> writeClient.fireEventWithOptions(evName, new EventOptions().setPayload(p1)));
    firstEvent.await(500);
    tc.assertEquals(p1, current.get());
    System.out.println("Got first event " + p1);
    Utils.getAsync(() -> writeClient.fireEventWithOptions(
      randomAlphaString(10),
      new EventOptions().setPayload(randomAlphaString(10))
    ));
    System.out.println("Sent event by a different name. Watcher should not receive any event");
    tc.assertEquals(p1, current.get());
    Utils.getAsync(() -> writeClient.fireEventWithOptions(evName, new EventOptions().setPayload(p2)));
    secondEvent.await(500);
    tc.assertEquals(p1 + "," + p2, current.get());
    System.out.println("Got second event " + p2);

    watch.stop();
  }

  @Test
  public void watchNodes(TestContext tc) {
    String nodeName = randomAlphaString(10);
    Async emptyNodes = tc.async(1);
    Async newNode = tc.async(1);
    Async emptyNodes2 = tc.async(2);

    Watch<NodeList> watch = Watch.nodes(vertx, consul.consulClientOptions(consul.dc().readToken()))
      .setHandler(list -> {
        if (list.succeeded()) {
          Optional<String> value = list.nextResult().getList()
            .stream().map(Node::getName).filter(s -> s.equals(nodeName))
            .findFirst();
          if (value.isPresent()) {
            tc.assertEquals(nodeName, value.get());
            newNode.countDown();
          } else {
            emptyNodes.countDown();
            emptyNodes2.countDown();
          }
        }
      }).start();

    emptyNodes.await(500);
    System.out.println("Empty nodes");

    ConsulInstance attached = ConsulInstance.defaultConsulBuilder(dc).nodeName(nodeName).join(consul).build();
    newNode.await(500);
    System.out.println("New node attached");
    attached.stop();
    emptyNodes2.await(500);
    System.out.println("New node disconnected");

    watch.stop();
  }

  @Test
  public void watchNodeHealthChecks(TestContext tc) {
    String serviceName = randomAlphaString(10);
    ServiceOptions opts = new ServiceOptions()
      .setName(serviceName)
      .setId(serviceName)
      .setCheckListOptions(Collections.singletonList(new CheckOptions()
        .setId("firstCheckFromList")
        .setName("firstCheckFromList")
        .setStatus(CheckStatus.PASSING)
        .setServiceId(serviceName)
        .setTtl("20s")))
      .setCheckOptions(new CheckOptions()
        .setId("singleCheck")
        .setName("singleCheck")
        .setStatus(CheckStatus.PASSING)
        .setServiceId(serviceName)
        .setTtl("15s"));
    runAsync(() -> writeClient.registerService(opts));
    List<Service> serviceList = getAsync(() -> writeClient.catalogServiceNodes(serviceName)).getList();
    tc.assertFalse(serviceList.isEmpty());
    Service service = serviceList.get(0);
    tc.assertNotNull(service);
    String nodeName = service.getNode();
    tc.assertNotNull(nodeName);
    Async passing = tc.async(1);
    Async failed = tc.async(2);
    Watch<CheckList> watch = Watch.nodeHealthChecks(
      nodeName, new CheckQueryOptions(), vertx, consul.consulClientOptions(consul.dc().readToken())
    ).setHandler(result -> {
      if (result.succeeded()) {
        List<Check> list = result.nextResult().getList();
        tc.assertEquals(3, list.size());
        if (list.stream().anyMatch(it -> it.getStatus() != CheckStatus.PASSING)) {
          failed.countDown();
          if (!failed.isCompleted()) {
            Optional<Check> singleCheck = list.stream()
              .filter(it -> Objects.equals(it.getId(), "singleCheck"))
              .findFirst();
            tc.assertTrue(singleCheck.isPresent());
            tc.assertEquals(CheckStatus.CRITICAL, singleCheck.get().getStatus());
          } else {
            Optional<Check> singleCheck = list.stream()
              .filter(it -> Objects.equals(it.getId(), "firstCheckFromList"))
              .findFirst();
            tc.assertTrue(singleCheck.isPresent());
            tc.assertEquals(CheckStatus.CRITICAL, singleCheck.get().getStatus());
          }
        } else passing.countDown();
      }
    });
    watch.start();
    passing.await(16_100);
    failed.await(21_000);
    watch.stop();
    runAsync(() -> writeClient.deregisterService(opts.getId()));
  }

  @Test
  public void watchServiceHealthChecks(TestContext tc) {
    String serviceName = randomAlphaString(10);
    ServiceOptions opts = new ServiceOptions()
      .setName(serviceName)
      .setId(serviceName)
      .setCheckListOptions(Collections.singletonList(new CheckOptions()
        .setId("firstCheckFromList")
        .setName("firstCheckFromList")
        .setStatus(CheckStatus.PASSING)
        .setServiceId(serviceName)
        .setTtl("20s")))
      .setCheckOptions(new CheckOptions()
        .setId("singleCheck")
        .setName("singleCheck")
        .setStatus(CheckStatus.PASSING)
        .setServiceId(serviceName)
        .setTtl("15s"));
    runAsync(() -> writeClient.registerService(opts));
    Async passing = tc.async(1);
    Async failed = tc.async(2);
    Watch<CheckList> watch = Watch.serviceHealthChecks(
      serviceName, new CheckQueryOptions(), vertx, consul.consulClientOptions(consul.dc().readToken())
    ).setHandler(result -> {
      if (result.succeeded()) {
        List<Check> list = result.nextResult().getList();
        tc.assertEquals(2, list.size());
        if (list.stream().anyMatch(it -> it.getStatus() != CheckStatus.PASSING)) {
          failed.countDown();
          if (!failed.isCompleted()) {
            Optional<Check> singleCheck = list.stream()
              .filter(it -> Objects.equals(it.getId(), "singleCheck"))
              .findFirst();
            tc.assertTrue(singleCheck.isPresent());
            tc.assertEquals(CheckStatus.CRITICAL, singleCheck.get().getStatus());
          } else {
            Optional<Check> singleCheck = list.stream()
              .filter(it -> Objects.equals(it.getId(), "firstCheckFromList"))
              .findFirst();
            tc.assertTrue(singleCheck.isPresent());
            tc.assertEquals(CheckStatus.CRITICAL, singleCheck.get().getStatus());
          }
        } else passing.countDown();
      }
    });
    watch.start();
    passing.await(16_000);
    failed.await(21_000);
    watch.stop();
    runAsync(() -> writeClient.deregisterService(opts.getId()));
  }
}

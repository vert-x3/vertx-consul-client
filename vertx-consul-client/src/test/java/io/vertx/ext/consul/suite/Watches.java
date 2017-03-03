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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.*;
import static io.vertx.test.core.TestUtils.randomAlphaString;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Watches extends ConsulTestBase {

  @Test
  public void throwStartStart() {
    Watch<KeyValue> watch = Watch.key("key", vertx);
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
    Watch<KeyValue> watch = Watch.key("key", vertx);
    watch.stop();
  }

  @Test(expected = IllegalStateException.class)
  public void throwStartStopStop() {
    Watch<KeyValue> watch = Watch.key("key", vertx);
    watch.start().stop();
    watch.stop();
  }

  @Test
  public void connectionRefused() throws InterruptedException {
    checkDelay(new ConsulClientOptions().setPort(Utils.getFreePort()), "Connection refused");
  }

  @Test
  public void keyNotFound() throws InterruptedException {
    checkDelay(readClientOptions, "Not Found");
  }

  private void checkDelay(ConsulClientOptions options, String err) throws InterruptedException {
    List<Long> ticks = new ArrayList<>();
    long t0 = System.currentTimeMillis();
    CountDownLatch latch = new CountDownLatch(5);
    Watch<KeyValue> watch = Watch.key("foo/bar/not/found", vertx, options)
      .setHandler(h -> {
        if (h.succeeded()) {
          fail();
        } else {
          assertTrue(h.failed());
          assertTrue(h.cause().getMessage().contains(err));
          ticks.add(System.currentTimeMillis() - t0);
          latch.countDown();
        }
      })
      .start();
    assertTrue(latch.await(2, TimeUnit.MINUTES));
    watch.stop();

    ticks.forEach(System.out::println);
    List<Long> diff;
    diff = diff(ticks); // parabolic
    diff = diff(diff);  // proportionality
    diff = diff(diff);  // constant (~2000)
    diff = diff(diff);  // zero
    System.out.println("zero: " + diff.get(0));
    assertTrue(Math.abs(diff.get(0)) < 1000);
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
    List<String> outs = new ArrayList<>();

    Watch<KeyValue> watch = Watch.key("foo/bar/w1", vertx, readClientOptions)
      .setHandler(kv -> {
        if (kv.succeeded()) {
          outs.add(kv.result().getValue());
        } else {
          outs.add(kv.cause().getMessage());
        }
      })
      .start();
    sleep(vertx, 3000);

    assertTrue(getAsync(h -> writeClient.putValue("foo/bar/w1", "v1", h)));
    sleep(vertx, 3000);
    assertTrue(getAsync(h -> writeClient.putValue("foo/bar/w1", "v2", h)));
    sleep(vertx, 3000);
    runAsync(h -> writeClient.deleteValue("foo/bar/w1", h));
    sleep(vertx, 3000);
    watch.stop();

    assertEquals(outs.get(0), "Not Found");  // immediately
    assertEquals(outs.get(1), "Not Found");  // second try after 1s delay
    assertEquals(outs.get(2), "v1");
    assertEquals(outs.get(3), "v2");
    assertEquals(outs.get(4), "Not Found");
  }

  @Test
  public void watchExistingKey() throws InterruptedException {
    List<String> outs = new ArrayList<>();

    assertTrue(getAsync(h -> writeClient.putValue("foo/bar/w2", "v1", h)));

    Watch<KeyValue> watch = Watch.key("foo/bar/w2", vertx, readClientOptions)
      .setHandler(kv -> {
        if (kv.succeeded()) {
          outs.add(kv.result().getValue());
        } else {
          outs.add(kv.cause().getMessage());
        }
      })
      .start();
    sleep(vertx, 1000);

    assertTrue(getAsync(h -> writeClient.putValue("foo/bar/w2", "v2", h)));
    sleep(vertx, 1000);
    runAsync(h -> writeClient.deleteValue("foo/bar/w2", h));
    sleep(vertx, 1000);
    watch.stop();

    assertEquals(outs.get(0), "v1");
    assertEquals(outs.get(1), "v2");
    assertEquals(outs.get(2), "Not Found");
  }

  @Test
  public void watchServices() {

    List<String> outs = new ArrayList<>();

    ServiceOptions service = new ServiceOptions()
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10));

    Watch<ServiceList> watch = Watch.services(vertx, readClientOptions)
      .setHandler(list -> {
        if (list.succeeded()) {
          outs.add(list.result().getList()
            .stream().map(Service::getName).filter(s -> s.equals(service.getName()))
            .findFirst().orElse(""));
        } else {
          outs.add(list.cause().getMessage());
        }
      })
      .start();
    sleep(vertx, 1000);

    runAsync(h -> writeClient.registerService(service, h));
    sleep(vertx, 1000);
    watch.stop();

    assertEquals(outs.get(0), "");
    assertEquals(outs.get(1), service.getName());

    runAsync(h -> writeClient.deregisterService(service.getId(), h));
  }

  @Test
  public void watchService() {

    List<String> outs = new ArrayList<>();

    ServiceOptions service = new ServiceOptions()
      .setCheckOptions(new CheckOptions()
        .setStatus(CheckStatus.PASSING)
        .setTtl("4s")
        .setName(randomAlphaString(10)))
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10));

    Watch<ServiceEntryList> watch = Watch.service(service.getName(), vertx, readClientOptions)
      .setHandler(list -> {
        if (list.succeeded()) {
          outs.add(list.result().getList()
            .stream().filter(s -> s.getService().getName().equals(service.getName()))
            .map(e -> e.getService().getName() + "/" + e.getChecks().stream()
              .filter(c -> c.getId().equals("service:" + service.getId()))
              .map(c -> c.getStatus().name()).findFirst().orElse(""))
            .findFirst().orElse(""));
        } else {
          outs.add(list.cause().getMessage());
        }
      })
      .start();
    sleep(vertx, 2000);

    runAsync(h -> writeClient.registerService(service, h));
    sleep(vertx, 10000);
    watch.stop();

    assertEquals(outs.get(0), "");
    assertEquals(outs.get(1), service.getName() + "/PASSING");
    assertEquals(outs.get(2), service.getName() + "/CRITICAL");

    runAsync(h -> writeClient.deregisterService(service.getId(), h));
  }
}

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

import io.vertx.core.Handler;
import io.vertx.ext.consul.*;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.tests.Utils.*;
import static io.vertx.test.core.TestUtils.randomAlphaString;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Checks extends ChecksBase {

  @Test
  public void bindCheckToService() {
    String serviceName = "serviceName";
    ServiceOptions service = new ServiceOptions()
      .setName(serviceName)
      .setAddress("10.0.0.1")
      .setPort(8080);
    runAsync(() -> writeClient.registerService(service));

    List<Service> services = getAsync(() -> writeClient.localServices());
    Service s = services.stream().filter(i -> "serviceName".equals(i.getName())).findFirst().get();
    String serviceId = s.getId();
    assertEquals(s.getAddress(), "10.0.0.1");
    assertEquals(s.getPort(), 8080);

    CheckOptions check = new CheckOptions()
      .setId("checkId")
      .setName("checkName")
      .setNotes("checkNotes")
      .setServiceId(serviceId)
      .setStatus(CheckStatus.PASSING)
      .setTtl("10s");
    runAsync(() -> writeClient.registerCheck(check));

    List<Check> checks = getAsync(() -> writeClient.localChecks());
    Check c = checks.stream().filter(i -> "checkId".equals(i.getId())).findFirst().get();
    assertEquals(c.getServiceId(), serviceId);
    assertEquals(c.getId(), "checkId");
    assertEquals(c.getStatus(), CheckStatus.PASSING);
    assertEquals(c.getNotes(), "checkNotes");

    runAsync(() -> writeClient.deregisterService(serviceId));
  }

  @Test
  public void healthState() throws InterruptedException {
    String serviceName = randomAlphaString(10);
    ServiceOptions opts = new ServiceOptions()
      .setName(serviceName)
      .setId(serviceName)
      .setCheckOptions(new CheckOptions().setTtl("1m"));
    runAsync(() -> masterClient.registerService(opts));
    CheckList list1 = getAsync(() -> masterClient.healthState(HealthState.CRITICAL));
    CountDownLatch latch = new CountDownLatch(1);
    waitBlockingQuery(latch, 10, list1.getIndex(), (idx, fut) -> {
      CheckQueryOptions options = new CheckQueryOptions()
        .setBlockingOptions(new BlockingQueryOptions().setIndex(idx));
      masterClient.healthStateWithOptions(HealthState.PASSING, options).onComplete(h -> {
        List<String> names = h.result().getList().stream().map(Check::getServiceName).collect(Collectors.toList());
        waitComplete(vertx, fut, h.result().getIndex(), names.contains(serviceName));
      });
    });
    runAsync(() -> masterClient.passCheck("service:" + serviceName));
    awaitLatch(latch);
    runAsync(() -> masterClient.deregisterService(serviceName));
  }

  @Test
  public void healthStateListChecks() throws InterruptedException {
    String serviceName = randomAlphaString(10);
    ServiceOptions opts = new ServiceOptions()
      .setName(serviceName)
      .setId(serviceName)
      .setCheckListOptions(Collections.singletonList(new CheckOptions().setTtl("1m")));
    runAsync(() -> writeClient.registerService(opts));
    CheckList list1 = getAsync(() -> readClient.healthState(HealthState.CRITICAL));
    CountDownLatch latch = new CountDownLatch(1);
    waitBlockingQuery(latch, 10, list1.getIndex(), (idx, fut) -> {
      CheckQueryOptions options = new CheckQueryOptions()
        .setBlockingOptions(new BlockingQueryOptions().setIndex(idx));
      readClient.healthStateWithOptions(HealthState.PASSING, options).onComplete(h -> {
        List<String> names = h.result().getList().stream().map(Check::getServiceName).collect(Collectors.toList());
        waitComplete(vertx, fut, h.result().getIndex(), names.contains(serviceName));
      });
    });
    runAsync(() -> writeClient.passCheck("service:" + serviceName));
    awaitLatch(latch);
    runAsync(() -> writeClient.deregisterService(serviceName));
  }

  @Test
  public void healthStateListChecksWithSingleCheck() throws InterruptedException {
    String serviceName = randomAlphaString(10);
    ServiceOptions opts = new ServiceOptions()
      .setName(serviceName)
      .setId(serviceName)
      .setCheckListOptions(Collections.singletonList(new CheckOptions()
        .setId("firstCheckFromList")
        .setTtl("20s")))
      .setCheckOptions(new CheckOptions()
        .setId("singleCheck")
        .setTtl("10s"));
    runAsync(() -> writeClient.registerService(opts));
    CheckList list1 = getAsync(() -> readClient.healthState(HealthState.CRITICAL));
    CountDownLatch latch = new CountDownLatch(1);
    waitBlockingQuery(latch, 10, list1.getIndex(), (idx, fut) -> {
      CheckQueryOptions options = new CheckQueryOptions()
        .setBlockingOptions(new BlockingQueryOptions().setIndex(idx));
      readClient.healthStateWithOptions(HealthState.PASSING, options).onComplete(h -> {
        List<String> names = h.result().getList().stream().map(Check::getServiceName).collect(Collectors.toList());
        waitComplete(vertx, fut, h.result().getIndex(), names.contains(serviceName));
      });
    });

    runAsync(() -> writeClient.passCheck("firstCheckFromList"));
    runAsync(() -> writeClient.passCheck("singleCheck"));
    awaitLatch(latch);
    runAsync(() -> writeClient.deregisterService(serviceName));
  }

  @Test
  public void healthChecks() throws InterruptedException {
    ServiceOptions opts = new ServiceOptions()
      .setName("serviceName")
      .setId("serviceId")
      .setTags(Collections.singletonList("tag1"));
    runAsync(() -> writeClient.registerService(opts));
    runAsync(() -> writeClient.registerCheck(new CheckOptions()
      .setTtl("10s")
      .setServiceId("serviceId")
      .setId("checkId1")
      .setName("checkName1")));

    CheckList list1 = getAsync(() -> readClient.healthChecks("serviceName"));
    assertEquals(list1.getList().size(), 1);
    assertEquals(list1.getList().get(0).getId(), "checkId1");

    CountDownLatch latch = new CountDownLatch(1);
    waitBlockingQuery(latch, 10, list1.getIndex(), (idx, fut) -> {
      CheckQueryOptions options = new CheckQueryOptions()
        .setBlockingOptions(new BlockingQueryOptions().setIndex(idx));
      readClient.healthChecksWithOptions("serviceName", options).onComplete(h -> {
        List<String> ids = h.result().getList().stream().map(Check::getId).collect(Collectors.toList());
        boolean success = h.result().getList().size() == 2;
        success &= ids.contains("checkId1");
        success &= ids.contains("checkId2");
        waitComplete(vertx, fut, h.result().getIndex(), success);
      });
    });
    sleep(vertx, 2000);
    assertEquals(latch.getCount(), 1);

    runAsync(() -> writeClient.registerCheck(new CheckOptions()
      .setTtl("10s")
      .setServiceId("serviceId")
      .setId("checkId2")
      .setName("checkName2")));

    awaitLatch(latch);

    runAsync(() -> writeClient.deregisterCheck("checkId1"));
    runAsync(() -> writeClient.deregisterCheck("checkId2"));
    runAsync(() -> writeClient.deregisterService("serviceId"));
  }

  @Override
  String createCheck(CheckOptions opts) {
    String id = opts.getId();
    if (id == null) {
      id = "checkId";
      opts.setId(id);
    }
    runAsync(() -> writeClient.registerCheck(opts));
    return id;
  }

  @Override
  void createCheck(TestContext tc, CheckOptions opts, Handler<String> idHandler) {
    String id = opts.getId() == null ? randomAlphaString(10) : opts.getId();
    opts.setId(id);
    writeClient.registerCheck(opts).onComplete(tc.asyncAssertSuccess(v -> idHandler.handle(id)));
  }

}

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
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.Utils.*;
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
    runAsync(h -> ctx.writeClient().registerService(service, h));

    List<Service> services = getAsync(h -> ctx.writeClient().localServices(h));
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
    runAsync(h -> ctx.writeClient().registerCheck(check, h));

    List<Check> checks = getAsync(h -> ctx.writeClient().localChecks(h));
    Check c = checks.stream().filter(i -> "checkId".equals(i.getId())).findFirst().get();
    assertEquals(c.getServiceId(), serviceId);
    assertEquals(c.getStatus(), CheckStatus.PASSING);
    assertEquals(c.getNotes(), "checkNotes");

    runAsync(h -> ctx.writeClient().deregisterService(serviceId, h));
  }

  @Test
  public void healthState() throws InterruptedException {
    String serviceName = randomAlphaString(10);
    ServiceOptions opts = new ServiceOptions()
      .setName(serviceName)
      .setId(serviceName)
      .setCheckOptions(new CheckOptions().setTtl("1m"));
    runAsync(h -> ctx.writeClient().registerService(opts, h));
    CheckList list1 = getAsync(h -> ctx.readClient().healthState(HealthState.CRITICAL, h));
    CountDownLatch latch = new CountDownLatch(1);
    waitBlockingQuery(latch, 10, list1.getIndex(), (idx, fut) -> {
      CheckQueryOptions options = new CheckQueryOptions()
        .setBlockingOptions(new BlockingQueryOptions().setIndex(idx));
      ctx.readClient().healthStateWithOptions(HealthState.PASSING, options, h -> {
        List<String> names = h.result().getList().stream().map(Check::getServiceName).collect(Collectors.toList());
        waitComplete(vertx, fut, h.result().getIndex(), names.contains(serviceName));
      });
    });
    runAsync(h -> ctx.writeClient().passCheck("service:" + serviceName, h));
    awaitLatch(latch);
    runAsync(h -> ctx.writeClient().deregisterService(serviceName, h));
  }

  @Test
  public void healthChecks() throws InterruptedException {
    ServiceOptions opts = new ServiceOptions()
      .setName("serviceName")
      .setId("serviceId")
      .setTags(Collections.singletonList("tag1"));
    runAsync(h -> ctx.writeClient().registerService(opts, h));
    runAsync(h -> ctx.writeClient().registerCheck(new CheckOptions()
      .setTtl("10s")
      .setServiceId("serviceId")
      .setId("checkId1")
      .setName("checkName1"), h));

    CheckList list1 = getAsync(h -> ctx.readClient().healthChecks("serviceName", h));
    assertEquals(list1.getList().size(), 1);
    assertEquals(list1.getList().get(0).getId(), "checkId1");

    CountDownLatch latch = new CountDownLatch(1);
    waitBlockingQuery(latch, 10, list1.getIndex(), (idx, fut) -> {
      CheckQueryOptions options = new CheckQueryOptions()
        .setBlockingOptions(new BlockingQueryOptions().setIndex(idx));
      ctx.readClient().healthChecksWithOptions("serviceName", options, h -> {
        List<String> ids = h.result().getList().stream().map(Check::getId).collect(Collectors.toList());
        boolean success = h.result().getList().size() == 2;
        success &= ids.contains("checkId1");
        success &= ids.contains("checkId2");
        waitComplete(vertx, fut, h.result().getIndex(), success);
      });
    });
    sleep(vertx, 2000);
    assertEquals(latch.getCount(), 1);

    runAsync(h -> ctx.writeClient().registerCheck(new CheckOptions()
      .setTtl("10s")
      .setServiceId("serviceId")
      .setId("checkId2")
      .setName("checkName2"), h));

    awaitLatch(latch);

    runAsync(h -> ctx.writeClient().deregisterCheck("checkId1", h));
    runAsync(h -> ctx.writeClient().deregisterCheck("checkId2", h));
    runAsync(h -> ctx.writeClient().deregisterService("serviceId", h));
  }

  @Override
  String createCheck(CheckOptions opts) {
    String id = opts.getId();
    if (id == null) {
      id = "checkId";
      opts.setId(id);
    }
    runAsync(h -> ctx.writeClient().registerCheck(opts, h));
    return id;
  }

  @Override
  void createCheck(TestContext tc, CheckOptions opts, Handler<String> idHandler) {
    String id = opts.getId() == null ? randomAlphaString(10) : opts.getId();
    opts.setId(id);
    ctx.writeClient().registerCheck(opts, tc.asyncAssertSuccess(v -> idHandler.handle(id)));
  }

}

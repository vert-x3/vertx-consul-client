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

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.*;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.RandomObjects.randomServiceOptions;
import static io.vertx.ext.consul.Utils.*;
import static io.vertx.test.core.TestUtils.randomAlphaString;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Services extends ChecksBase {

  @Test
  public void createLocalService(TestContext tc) {
    String serviceName = randomAlphaString(10);
    ServiceOptions service = randomServiceOptions().setName(serviceName).setId(null);
    ctx.writeClient().registerService(service, tc.asyncAssertSuccess(reg -> {
      ctx.writeClient().localServices(tc.asyncAssertSuccess(services -> {
        Service s = services.stream()
          .filter(i -> serviceName.equals(i.getName()))
          .findFirst()
          .orElseThrow(NoSuchElementException::new);
        String serviceId = s.getId();
        tc.assertEquals(s.getTags(), service.getTags());
        tc.assertEquals(s.getAddress(), service.getAddress());
        tc.assertEquals(s.getPort(), service.getPort());
        ctx.writeClient().localChecks(tc.asyncAssertSuccess(checks -> {
          Check c = checks.stream()
            .filter(i -> serviceName.equals(i.getServiceName()))
            .findFirst()
            .orElseThrow(NoSuchElementException::new);
          tc.assertEquals(c.getId(), "service:" + serviceName);
          tc.assertEquals(c.getNotes(), service.getCheckOptions().getNotes());
          ctx.writeClient().catalogNodeServices(ctx.nodeName(), tc.asyncAssertSuccess(nodeServices -> {
            tc.assertEquals(2, nodeServices.getList().size());
            Async async = tc.async(2);
            ServiceQueryOptions knownOpts = new ServiceQueryOptions().setTag(service.getTags().get(0));
            ctx.writeClient().catalogServiceNodesWithOptions(serviceName, knownOpts, tc.asyncAssertSuccess(nodeServicesWithKnownTag -> {
              tc.assertEquals(1, nodeServicesWithKnownTag.getList().size());
              async.countDown();
            }));
            ServiceQueryOptions unknownOpts = new ServiceQueryOptions().setTag("unknownTag");
            ctx.writeClient().catalogServiceNodesWithOptions(serviceName, unknownOpts, tc.asyncAssertSuccess(nodeServicesWithUnknownTag -> {
              tc.assertEquals(0, nodeServicesWithUnknownTag.getList().size());
              async.countDown();
            }));
            async.handler(v -> {
              ctx.writeClient().deregisterService(serviceId, tc.asyncAssertSuccess(deregistered -> {
                ctx.writeClient().localServices(tc.asyncAssertSuccess(cleaned -> {
                  tc.assertEquals(cleaned.stream()
                    .filter(i -> serviceName.equals(i.getName()))
                    .count(), 0L);
                }));
              }));
            });
          }));
        }));
      }));
    }));
  }

  @Test(timeout = 3 * 60 * 1000)
  public void deregisterAfter(TestContext tc) {
    if (System.getProperty("skipDeregisterAfter") != null) {
      System.out.println("skip");
      return;
    }
    CheckOptions opts = new CheckOptions()
      .setDeregisterAfter("1m")
      .setStatus(CheckStatus.PASSING)
      .setTtl("10s")
      .setName("checkName");
    Async async = tc.async();
    createCheck(tc, opts, checkId -> {
      getCheckInfo(tc, checkId, passing -> {
        tc.assertEquals(CheckStatus.PASSING, passing.getStatus());
        vertx.setTimer(30000, l1 -> {
          getCheckInfo(tc, checkId, critical -> {
            tc.assertEquals(CheckStatus.CRITICAL, critical.getStatus());
            vertx.setTimer(90000, l2 -> {
              ctx.writeClient().localChecks(tc.asyncAssertSuccess(checks -> {
                tc.assertEquals(checks.stream().filter(c -> c.getName().equals("checkName")).count(), (long) 0);
                async.complete();
              }));
            });
          });
        });
      });
    });
  }

  @Test
  public void healthServices() throws InterruptedException {
    runAsync(h -> ctx.writeClient().registerService(new ServiceOptions()
      .setName("service").setId("id1").setTags(Collections.singletonList("tag1"))
      .setCheckOptions(new CheckOptions().setTtl("5s").setStatus(CheckStatus.PASSING)), h));
    runAsync(h -> ctx.writeClient().registerService(new ServiceOptions()
      .setName("service").setId("id2").setTags(Collections.singletonList("tag2"))
      .setCheckOptions(new CheckOptions().setTtl("5s").setStatus(CheckStatus.PASSING)), h));

    ServiceEntryList list1 = getAsync(h -> ctx.readClient().healthServiceNodes("service", true, h));
    assertEquals(list1.getList().size(), 2);
    List<String> ids = list1.getList().stream().map(entry -> entry.getService().getId()).collect(Collectors.toList());
    assertTrue(ids.contains("id1"));
    assertTrue(ids.contains("id2"));

    ServiceQueryOptions opts2 = new ServiceQueryOptions().setTag("tag2");
    ServiceEntryList list2 = getAsync(h -> ctx.readClient().healthServiceNodesWithOptions("service", true, opts2, h));
    assertEquals(list2.getList().size(), 1);
    assertEquals(list2.getList().get(0).getService().getId(), "id2");

    CountDownLatch latch = new CountDownLatch(1);
    waitBlockingQuery(latch, 10, list1.getIndex(), (idx, fut) -> {
      ServiceQueryOptions options = new ServiceQueryOptions()
        .setBlockingOptions(new BlockingQueryOptions().setIndex(idx));
      ctx.readClient().healthServiceNodesWithOptions("service", true, options, h -> {
        waitComplete(vertx, fut, h.result().getIndex(), h.result().getList().size() == 1);
      });
    });
    sleep(vertx, 2000);
    assertEquals(latch.getCount(), 1);
    runAsync(h -> ctx.writeClient().failCheck("service:id1", h));
    awaitLatch(latch);
    runAsync(h -> ctx.writeClient().deregisterService("id1", h));
    runAsync(h -> ctx.writeClient().deregisterService("id2", h));
  }

  @Test
  public void findConsul() {
    ServiceList localConsulList = getAsync(h -> ctx.writeClient().catalogServiceNodes("consul", h));
    assertEquals(localConsulList.getList().size(), 1);
    List<Service> catalogConsulList = Utils.<ServiceList>getAsync(h -> ctx.writeClient().catalogServices(h))
      .getList().stream().filter(s -> s.getName().equals("consul")).collect(Collectors.toList());
    assertEquals(1, catalogConsulList.size());
    assertEquals(0, catalogConsulList.get(0).getTags().size());
  }

  @Test
  public void maintenanceMode() {
    String serviceId = "serviceId";
    ServiceOptions service = new ServiceOptions()
      .setName("serviceName")
      .setId(serviceId)
      .setAddress("10.0.0.1")
      .setCheckOptions(new CheckOptions().setTtl("1h"))
      .setPort(8080);
    runAsync(h -> ctx.writeClient().registerService(service, h));
    runAsync(h -> ctx.writeClient().passCheck("service:" + serviceId, h));

    List<Check> checks = getAsync(h -> ctx.writeClient().localChecks(h));
    assertEquals(1, checks.size());

    String reason = "special symbols like `&` are allowed (хорошо)";
    MaintenanceOptions opts = new MaintenanceOptions()
      .setId(serviceId)
      .setReason(reason)
      .setEnable(true);
    runAsync(h -> ctx.writeClient().maintenanceService(opts, h));

    // TODO undocumented (?) behavior
    checks = getAsync(h -> ctx.writeClient().localChecks(h));
    assertEquals(2, checks.size());
    long cnt = checks.stream().filter(info -> info.getStatus() == CheckStatus.CRITICAL).count();
    assertEquals(1, cnt);
    assertEquals(reason, checks.get(0).getNotes());

    opts.setEnable(false);
    runAsync(h -> ctx.writeClient().maintenanceService(opts, h));

    checks = getAsync(h -> ctx.writeClient().localChecks(h));
    assertEquals(1, checks.size());

    runAsync(h -> ctx.writeClient().deregisterService(serviceId, h));
  }

  @Test
  public void catalogServicesBlocking() throws InterruptedException {
    testServicesBlocking(h -> ctx.readClient().catalogServices(h),
      (opts, h) -> ctx.readClient().catalogServicesWithOptions(opts, h));
  }

  @Test
  public void catalogNodeServicesBlocking() throws InterruptedException {
    testServicesBlocking(h -> ctx.readClient().catalogNodeServices(ctx.nodeName(), h),
      (opts, h) -> ctx.readClient().catalogNodeServicesWithOptions(ctx.nodeName(), opts, h));
  }

  private void testServicesBlocking(Consumer<Handler<AsyncResult<ServiceList>>> runner,
                                    BiConsumer<BlockingQueryOptions, Handler<AsyncResult<ServiceList>>> request) throws InterruptedException {
    runAsync(h -> ctx.writeClient().registerService(new ServiceOptions().setName("service1").setId("id1"), h));
    ServiceList list1 = getAsync(runner);
    list1.getList().forEach(s -> System.out.println("--- " + s.toJson().encode()));
    CountDownLatch latch = new CountDownLatch(1);
    waitBlockingQuery(latch, 10, list1.getIndex(), (idx, fut) -> {
      request.accept(new BlockingQueryOptions().setIndex(idx), h -> {
        h.result().getList().forEach(s -> System.out.println("-+- " + s.toJson().encode()));
        List<String> names = h.result().getList().stream().map(Service::getName).collect(Collectors.toList());
        waitComplete(vertx, fut, h.result().getIndex(), names.contains("service2"));
      });
    });
    sleep(vertx, 4000);
    assertEquals(latch.getCount(), 1);
    runAsync(h -> ctx.writeClient().registerService(new ServiceOptions().setName("service2").setId("id2"), h));
    awaitLatch(latch);
    runAsync(h -> ctx.writeClient().deregisterService("id1", h));
    runAsync(h -> ctx.writeClient().deregisterService("id2", h));
  }

  @Override
  String createCheck(CheckOptions opts) {
    String serviceId = "serviceId";
    ServiceOptions service = new ServiceOptions()
      .setName("serviceName")
      .setId(serviceId)
      .setTags(Arrays.asList("tag1", "tag2"))
      .setCheckOptions(opts)
      .setAddress("10.0.0.1")
      .setPort(8080);
    runAsync(h -> ctx.writeClient().registerService(service, h));
    return "service:" + serviceId;
  }

  @Override
  void createCheck(TestContext tc, CheckOptions opts, Handler<String> idHandler) {
    ServiceOptions options = randomServiceOptions().setCheckOptions(opts);
    ctx.writeClient().registerService(options, tc.asyncAssertSuccess(v -> idHandler.handle("service:" + options.getId())));
  }
}

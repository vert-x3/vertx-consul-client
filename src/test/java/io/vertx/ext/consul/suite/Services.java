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

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.RandomObjects.randomServiceOptions;
import static io.vertx.ext.consul.Utils.*;
import static io.vertx.test.core.TestUtils.randomAlphaString;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Services extends ChecksBase {

  private static String checkService(TestContext tc, List<Service> list, String name, ServiceOptions options) {
    Service s = list.stream()
      .filter(i -> name.equals(i.getName()))
      .findFirst()
      .orElseThrow(NoSuchElementException::new);
    String serviceId = s.getId();
    tc.assertEquals(s.getTags(), options.getTags());
    tc.assertEquals(s.getAddress(), options.getAddress());
    tc.assertEquals(s.getPort(), options.getPort());
    for (Map.Entry<String, String> entry : options.getMeta().entrySet()) {
      tc.assertEquals(s.getMeta().get(entry.getKey()), entry.getValue());
    }
    return serviceId;
  }

  @Test
  public void createLocalService(TestContext tc) {
    String serviceName = randomAlphaString(10);
    ServiceOptions opts = randomServiceOptions().setName(serviceName).setId(null);
    writeClient.registerService(opts, tc.asyncAssertSuccess(reg -> {
      writeClient.localServices(tc.asyncAssertSuccess(services -> {
        String serviceId = checkService(tc, services, serviceName, opts);
        writeClient.localChecks(tc.asyncAssertSuccess(checks -> {
          Check c = checks.stream()
            .filter(i -> serviceName.equals(i.getServiceName()))
            .findFirst()
            .orElseThrow(NoSuchElementException::new);
          tc.assertEquals(c.getId(), opts.getCheckOptions().getId());
          tc.assertEquals(c.getNotes(), opts.getCheckOptions().getNotes());
          writeClient.catalogNodeServices(consul.getConfig("node_name"), tc.asyncAssertSuccess(nodeServices -> {
            tc.assertEquals(2, nodeServices.getList().size());
            checkService(tc, nodeServices.getList(), serviceName, opts);
            Async async = tc.async(2);
            ServiceQueryOptions knownOpts = new ServiceQueryOptions().setTag(opts.getTags().get(0));
            writeClient.catalogServiceNodesWithOptions(
              serviceName,
              knownOpts,
              tc.asyncAssertSuccess(nodeServicesWithKnownTag -> {
                tc.assertEquals(1, nodeServicesWithKnownTag.getList().size());
                async.countDown();
              })
            );
            ServiceQueryOptions unknownOpts = new ServiceQueryOptions().setTag("unknownTag");
            writeClient.catalogServiceNodesWithOptions(
              serviceName,
              unknownOpts,
              tc.asyncAssertSuccess(nodeServicesWithUnknownTag -> {
                tc.assertEquals(0, nodeServicesWithUnknownTag.getList().size());
                async.countDown();
              })
            );
            async.handler(v -> {
              writeClient.deregisterService(serviceId, tc.asyncAssertSuccess(deregistered -> {
                writeClient.localServices(tc.asyncAssertSuccess(cleaned -> {
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
              writeClient.localChecks(tc.asyncAssertSuccess(checks -> {
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
    runAsync(h -> writeClient.registerService(new ServiceOptions()
      .setName("service").setId("id1").setTags(Collections.singletonList("tag1"))
      .setCheckOptions(new CheckOptions().setTtl("5s").setStatus(CheckStatus.PASSING)), h));
    runAsync(h -> writeClient.registerService(new ServiceOptions()
      .setName("service").setId("id2").setTags(Collections.singletonList("tag2"))
      .setCheckOptions(new CheckOptions().setTtl("5s").setStatus(CheckStatus.PASSING)), h));

    runAsync(h -> writeClient.registerService(new ServiceOptions()
      .setName("service").setId("id3").setTags(Collections.singletonList("tag3"))
      .setCheckListOptions(new ArrayList(Arrays.asList(
        new CheckOptions()
          .setId("firstCheck")
          .setTtl("5s")
          .setStatus(CheckStatus.PASSING),
        new CheckOptions()
          .setId("secondCheck")
          .setTtl("15s")
          .setStatus(CheckStatus.PASSING)
      ))), h));

    ServiceEntryList list1 = getAsync(h -> readClient.healthServiceNodes("service", true, h));
    assertEquals(list1.getList().size(), 3);
    List<String> ids = list1.getList().stream().map(entry -> entry.getService().getId()).collect(Collectors.toList());
    Optional<String> nodeId = list1
      .getList()
      .stream()
      .map(entry -> entry.getNode().getName())
      .findFirst();
    assertTrue(ids.contains("id1"));
    assertTrue(ids.contains("id2"));
    assertTrue(ids.contains("id3"));

    assertTrue(nodeId.isPresent());
    CheckList nodeHealthChecks = getAsync(h -> readClient.healthNodesWithOptions(nodeId.get(), new CheckQueryOptions(), h));
    assertEquals(5, nodeHealthChecks.getList().size());
    assertTrue(nodeHealthChecks.getList().stream().allMatch(it -> it.getStatus() == CheckStatus.PASSING));

    ServiceQueryOptions opts2 = new ServiceQueryOptions().setTag("tag2");
    ServiceEntryList list2 = getAsync(h -> readClient.healthServiceNodesWithOptions("service", true, opts2, h));
    assertEquals(list2.getList().size(), 1);
    assertEquals(list2.getList().get(0).getService().getId(), "id2");

    CountDownLatch latch = new CountDownLatch(1);
    waitBlockingQuery(latch, 10, list1.getIndex(), (idx, fut) -> {
      ServiceQueryOptions options = new ServiceQueryOptions()
        .setBlockingOptions(new BlockingQueryOptions().setIndex(idx));
      readClient.healthServiceNodesWithOptions("service", true, options, h -> {
        waitComplete(vertx, fut, h.result().getIndex(), h.result().getList().size() == 1);
      });
    });
    sleep(vertx, 2000);
    assertEquals(latch.getCount(), 1);
    runAsync(h -> writeClient.failCheck("service:id1", h));
    awaitLatch(latch);
    CheckList checksWithFailed = getAsync(h -> readClient.healthNodesWithOptions(nodeId.get(), new CheckQueryOptions(), h));
    assertEquals(5, nodeHealthChecks.getList().size());
    Optional<Check> failedCheck = checksWithFailed.getList().stream()
      .filter(it -> Objects.equals(it.getId(), "service:id1"))
      .findFirst();
    assertTrue(failedCheck.isPresent());
    assertEquals(CheckStatus.CRITICAL, failedCheck.get().getStatus());
    runAsync(h -> writeClient.deregisterService("id1", h));
    runAsync(h -> writeClient.deregisterService("id2", h));
    runAsync(h -> writeClient.deregisterService("id3", h));
  }

  @Test
  public void findConsul() {
    ServiceList localConsulList = getAsync(h -> writeClient.catalogServiceNodes("consul", h));
    assertEquals(localConsulList.getList().size(), 1);
    List<Service> catalogConsulList = Utils.<ServiceList>getAsync(h -> writeClient.catalogServices(h))
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
    runAsync(h -> writeClient.registerService(service, h));
    runAsync(h -> writeClient.passCheck("service:" + serviceId, h));

    List<Check> checks = getAsync(h -> writeClient.localChecks(h));
    assertEquals(1, checks.size());

    String reason = "special symbols like `&` are allowed (хорошо)";
    MaintenanceOptions opts = new MaintenanceOptions()
      .setId(serviceId)
      .setReason(reason)
      .setEnable(true);
    runAsync(h -> writeClient.maintenanceService(opts, h));

    // TODO undocumented (?) behavior
    checks = getAsync(h -> writeClient.localChecks(h));
    assertEquals(2, checks.size());
    long cnt = checks.stream().filter(info -> info.getStatus() == CheckStatus.CRITICAL).count();
    assertEquals(1, cnt);
    assertEquals(reason, checks.get(0).getNotes());

    opts.setEnable(false);
    runAsync(h -> writeClient.maintenanceService(opts, h));

    checks = getAsync(h -> writeClient.localChecks(h));
    assertEquals(1, checks.size());

    runAsync(h -> writeClient.deregisterService(serviceId, h));
  }

  @Test
  public void catalogServicesBlocking() throws InterruptedException {
    testServicesBlocking(
      h -> readClient.catalogServices(h),
      (opts, h) -> readClient.catalogServicesWithOptions(opts, h)
    );
  }

  @Test
  public void catalogNodeServicesBlocking() throws InterruptedException {
    testServicesBlocking(
      h -> readClient.catalogNodeServices(consul.getConfig("node_name"), h),
      (opts, h) -> readClient.catalogNodeServicesWithOptions(consul.getConfig("node_name"), opts, h)
    );
  }

  private void testServicesBlocking(
    Consumer<Handler<AsyncResult<ServiceList>>> runner,
    BiConsumer<BlockingQueryOptions, Handler<AsyncResult<ServiceList>>> request
  ) throws InterruptedException {
    runAsync(h -> writeClient.registerService(new ServiceOptions()
      .setName("service1")
      .setId("id1")
      .setPort(Utils.getFreePort()), h)
    );
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
    runAsync(h -> writeClient.registerService(new ServiceOptions()
      .setName("service2")
      .setId("id2")
      .setPort(Utils.getFreePort()), h)
    );
    awaitLatch(latch);
    runAsync(h -> writeClient.deregisterService("id1", h));
    runAsync(h -> writeClient.deregisterService("id2", h));
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
    runAsync(h -> writeClient.registerService(service, h));
    return "service:" + serviceId;
  }

  @Override
  void createCheck(TestContext tc, CheckOptions opts, Handler<String> idHandler) {
    ServiceOptions options = randomServiceOptions().setCheckOptions(opts);
    writeClient.registerService(options, tc.asyncAssertSuccess(v -> idHandler.handle("service:" + options.getId())));
  }
}

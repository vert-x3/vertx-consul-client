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

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.PreparedQueryDefinition;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class PreparedQuery extends ConsulTestBase {

  @Test
  public void createUpdateAndDestroy(TestContext tc) {
    Async async = tc.async();
    String service1 = randomFooBarAlpha();
    String service2 = randomFooBarAlpha();
    ctx.rxWriteClient()
      .rxCreatePreparedQuery(new PreparedQueryDefinition().setService(service1))
      .flatMap(id -> ctx.rxReadClient()
        .rxGetPreparedQuery(id)
        .map(check(q -> tc.assertTrue(q.getService().equals(service1))))
        .flatMap(def -> ctx.rxWriteClient().rxUpdatePreparedQuery(def.setService(service2)))
        .flatMap(v -> ctx.rxReadClient()
          .rxGetPreparedQuery(id)
          .map(check(q -> tc.assertTrue(q.getService().equals(service2)))))
        .flatMap(list -> ctx.rxWriteClient().rxDeletePreparedQuery(id)))
      .subscribe(o -> async.complete(), tc::fail);
  }

  @Test
  public void execute(TestContext tc) {
    Async async = tc.async();
    PreparedQueryDefinition def = new PreparedQueryDefinition()
      .setService("service-${match(1)}-${match(2)}")
      .setTemplateType("name_prefix_match")
      .setTemplateRegexp("^find_(.+?)_(.+?)$");
    ctx.rxWriteClient().rxCreatePreparedQuery(def)
      .flatMap(qid ->
        ctx.rxWriteClient().rxExecutePreparedQuery("find_1_2")
          .map(check(resp -> tc.assertEquals(resp.getNodes().size(), 0)))
          .flatMap(resp -> ctx.rxWriteClient()
            .rxRegisterService(new ServiceOptions().setName("service-1-2")))
          .flatMap(v -> ctx.rxWriteClient()
            .rxExecutePreparedQuery("find_1_2")
            .map(check(resp -> tc.assertEquals(resp.getNodes().size(), 1))))
          .flatMap(resp -> ctx.rxWriteClient()
            .rxDeregisterService("service-1-2"))
          .flatMap(v -> ctx.rxWriteClient()
            .rxDeletePreparedQuery(qid)))
      .subscribe(o -> async.complete(), tc::fail);
  }
}

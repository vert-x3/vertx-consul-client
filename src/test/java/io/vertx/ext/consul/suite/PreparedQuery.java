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
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.vertx.ext.consul.RandomObjects.randomPreparedQueryDefinition;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class PreparedQuery extends ConsulTestBase {

  @Test
  public void createUpdateAndDestroy(TestContext tc) {
    String service1 = randomFooBarAlpha();
    String service2 = randomFooBarAlpha();
    ctx.writeClient()
      .createPreparedQuery(new PreparedQueryDefinition().setService(service1), tc.asyncAssertSuccess(id -> {
        ctx.readClient()
          .getPreparedQuery(id, tc.asyncAssertSuccess(def1 -> {
            tc.assertTrue(def1.getService().equals(service1));
            ctx.writeClient().updatePreparedQuery(def1.setService(service2), tc.asyncAssertSuccess(updated -> {
              ctx.readClient().getPreparedQuery(id, tc.asyncAssertSuccess(def2 -> {
                tc.assertTrue(def2.getService().equals(service2));
                ctx.writeClient().deletePreparedQuery(id, tc.asyncAssertSuccess());
              }));
            }));
          }));
      }));
  }

  @Test
  public void checkDefinition(TestContext tc) {
    ctx.writeClient().createSession(tc.asyncAssertSuccess(sessId -> {
      PreparedQueryDefinition expected = randomPreparedQueryDefinition()
        .setId("")
        .setTemplateType("name_prefix_match")
        .setTemplateRegexp("^find_(.+?)_(.+?)$")
        .setSession(sessId);
      ctx.writeClient().createPreparedQuery(expected, tc.asyncAssertSuccess(id -> {
        ctx.readClient().getPreparedQuery(id, tc.asyncAssertSuccess(actual -> {
          tc.assertEquals(expected.getService(), actual.getService());
          tc.assertEquals(expected.getDcs(), actual.getDcs());
          tc.assertEquals(expected.getDnsTtl(), actual.getDnsTtl());
          tc.assertEquals(expected.getMeta(), actual.getMeta());
          tc.assertEquals(expected.getName(), actual.getName());
          tc.assertEquals(expected.getNearestN(), actual.getNearestN());
          tc.assertEquals(expected.getPassing(), actual.getPassing());
          tc.assertEquals(expected.getSession(), actual.getSession());
          tc.assertEquals(expected.getTags(), actual.getTags());
          tc.assertEquals(expected.getTemplateRegexp(), actual.getTemplateRegexp());
          tc.assertEquals(expected.getTemplateType(), actual.getTemplateType());
          ctx.writeClient().deletePreparedQuery(actual.getId(), tc.asyncAssertSuccess(v -> {
            ctx.writeClient().destroySession(expected.getSession(), tc.asyncAssertSuccess());
          }));
        }));
      }));
    }));
  }

  @Test
  public void execute(TestContext tc) {
    PreparedQueryDefinition def = new PreparedQueryDefinition()
      .setService("service-${match(1)}-${match(2)}")
      .setTemplateType("name_prefix_match")
      .setTemplateRegexp("^find_(.+?)_(.+?)$");
    ctx.writeClient().createPreparedQuery(def, tc.asyncAssertSuccess(qid -> {
      ctx.writeClient().executePreparedQuery("find_1_2", tc.asyncAssertSuccess(resp1 -> {
        tc.assertEquals(resp1.getNodes().size(), 0);
        ctx.writeClient()
          .registerService(new ServiceOptions().setName("service-1-2"), tc.asyncAssertSuccess(v1 -> {
            ctx.writeClient()
              .executePreparedQuery("find_1_2", tc.asyncAssertSuccess(resp2 -> {
                tc.assertEquals(resp2.getNodes().size(), 1);
                ctx.writeClient()
                  .deregisterService("service-1-2", tc.asyncAssertSuccess(v2 -> {
                    ctx.writeClient()
                      .deletePreparedQuery(qid, tc.asyncAssertSuccess());
                  }));
              }));
          }));
      }));
    }));
  }
}

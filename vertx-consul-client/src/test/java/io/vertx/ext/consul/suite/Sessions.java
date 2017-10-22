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
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class Sessions extends ConsulTestBase {

  @Test
  public void createDefaultSession(TestContext tc) {
    ctx.writeClient().createSession(tc.asyncAssertSuccess(id -> {
      ctx.writeClient().infoSession(id, tc.asyncAssertSuccess(session -> {
        tc.assertEquals(id, session.getId());
        tc.assertEquals(ctx.nodeName(), session.getNode());
        ctx.writeClient().destroySession(id, tc.asyncAssertSuccess());
      }));
    }));
  }

  @Test
  public void createSessionWithOptions(TestContext tc) {
    SessionOptions opt = new SessionOptions()
      .setBehavior(SessionBehavior.DELETE)
      .setLockDelay(42)
      .setName("optName")
      .setTtl(442);
    ctx.writeClient().createSessionWithOptions(opt, tc.asyncAssertSuccess(id -> {
      ctx.writeClient().infoSession(id, tc.asyncAssertSuccess(session -> {
        List<String> checks = session.getChecks();
        tc.assertEquals(1, checks.size());
        tc.assertTrue("serfHealth".equals(checks.get(0)));
        tc.assertEquals(opt.getLockDelay(), session.getLockDelay());
        tc.assertEquals(ctx.nodeName(), session.getNode());
        ctx.writeClient().destroySession(id, tc.asyncAssertSuccess());
      }));
    }));
  }

  @Test
  public void unknownNode(TestContext tc) {
    ctx.writeClient().createSessionWithOptions(new SessionOptions().setNode("unknownNode"), tc.asyncAssertFailure());
  }

  @Test
  public void unknownSession(TestContext tc) {
    ctx.writeClient().infoSession("00000000-0000-0000-0000-000000000000", tc.asyncAssertFailure());
  }

  @Test
  public void listSessions(TestContext tc) {
    ctx.writeClient().createSession(tc.asyncAssertSuccess(id -> {
      ctx.writeClient().infoSession(id, tc.asyncAssertSuccess(session -> {
        ctx.writeClient().listSessions(tc.asyncAssertSuccess(list -> {
          tc.assertEquals(session.getId(), list.getList().get(0).getId());
          ctx.writeClient().listNodeSessions(session.getNode(), tc.asyncAssertSuccess(nodeSesions -> {
            tc.assertEquals(session.getId(), nodeSesions.getList().get(0).getId());
            ctx.writeClient().destroySession(id, tc.asyncAssertSuccess());
          }));
        }));
      }));
    }));
  }

  @Test
  public void listSessionsBlocking(TestContext tc) throws InterruptedException {
    testSessionsBlocking(tc, (opts, h) -> ctx.readClient().listSessionsWithOptions(opts, h));
  }

  @Test
  public void listNodeSessionsBlocking(TestContext tc) throws InterruptedException {
    testSessionsBlocking(tc, (opts, h) -> ctx.readClient().listNodeSessionsWithOptions(ctx.nodeName(), opts, h));
  }

  private void testSessionsBlocking(TestContext tc, BiConsumer<BlockingQueryOptions, Handler<AsyncResult<SessionList>>> request) {
    ctx.writeClient().createSession(tc.asyncAssertSuccess(id1 -> {
      ctx.readClient().listSessions(tc.asyncAssertSuccess(list1 -> {
        Async async = tc.async();
        request.accept(new BlockingQueryOptions().setIndex(list1.getIndex()), h -> {
          List<String> ids = h.result().getList().stream().map(Session::getId).collect(Collectors.toList());
          assertTrue(ids.contains(id1));
          async.countDown();
        });
        vertx.setTimer(1000, l -> {
          assertEquals(async.count(), 1);
          ctx.writeClient().createSession(tc.asyncAssertSuccess(id2 -> {
            async.handler(a -> {
              ctx.writeClient().destroySession(id1, tc.asyncAssertSuccess(d1 -> {
                ctx.writeClient().destroySession(id2, tc.asyncAssertSuccess());
              }));
            });
          }));
        });
      }));
    }));
  }

  @Test
  public void sessionInfoBlocking(TestContext tc) {
    ctx.writeClient().createSession(tc.asyncAssertSuccess(id -> {
      ctx.readClient().infoSession(id, tc.asyncAssertSuccess(s1 -> {
        Async async = tc.async();
        ctx.readClient().infoSessionWithOptions(id, new BlockingQueryOptions().setIndex(s1.getIndex()), h -> async.countDown());
        vertx.setTimer(1000, l -> {
          assertEquals(async.count(), 1);
          ctx.writeClient().destroySession(id, tc.asyncAssertSuccess());
        });
      }));
    }));
  }

  @Test
  public void deleteBehavior(TestContext tc) {
    ctx.writeClient().createSessionWithOptions(new SessionOptions().setTtl(442).setBehavior(SessionBehavior.DELETE), tc.asyncAssertSuccess(id -> {
      ctx.writeClient().putValueWithOptions("foo/bar", "value1", new KeyValueOptions().setAcquireSession(id), tc.asyncAssertSuccess(b -> {
        tc.assertTrue(b);
        ctx.writeClient().getValue("foo/bar", tc.asyncAssertSuccess(pair -> {
          tc.assertEquals("value1", pair.getValue());
          tc.assertEquals(id, pair.getSession());
          ctx.writeClient().destroySession(id, tc.asyncAssertSuccess(v -> {
            ctx.writeClient().getValue("foo/bar", tc.asyncAssertSuccess(notfound -> {
              tc.assertFalse(notfound.isPresent());
            }));
          }));
        }));
      }));
    }));
  }
}

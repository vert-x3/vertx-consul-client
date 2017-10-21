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
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.Utils.*;

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
  public void listSessionsBlocking() throws InterruptedException {
    testSessionsBlocking((opts, h) -> ctx.readClient().listSessionsWithOptions(opts, h));
  }

  @Test
  public void listNodeSessionsBlocking() throws InterruptedException {
    testSessionsBlocking((opts, h) -> ctx.readClient().listNodeSessionsWithOptions(ctx.nodeName(), opts, h));
  }

  private void testSessionsBlocking(BiConsumer<BlockingQueryOptions, Handler<AsyncResult<SessionList>>> request) throws InterruptedException {
    String id1 = getAsync(h -> ctx.writeClient().createSession(h));
    SessionList list1 = getAsync(h -> ctx.readClient().listSessions(h));
    CountDownLatch latch = new CountDownLatch(1);
    request.accept(new BlockingQueryOptions().setIndex(list1.getIndex()), h -> {
      List<String> ids = h.result().getList().stream().map(Session::getId).collect(Collectors.toList());
      assertTrue(ids.contains(id1));
      latch.countDown();
    });
    sleep(vertx, 2000);
    assertEquals(latch.getCount(), 1);
    String id2 = getAsync(h -> ctx.writeClient().createSession(h));
    awaitLatch(latch);
    runAsync(h -> ctx.writeClient().destroySession(id1, h));
    runAsync(h -> ctx.writeClient().destroySession(id2, h));
  }

  @Test
  public void sessionInfoBlocking() throws InterruptedException {
    String id = getAsync(h -> ctx.writeClient().createSession(h));
    Session s1 = getAsync(h -> ctx.readClient().infoSession(id, h));
    CountDownLatch latch = new CountDownLatch(1);
    ctx.readClient().infoSessionWithOptions(id, new BlockingQueryOptions().setIndex(s1.getIndex()), h -> latch.countDown());
    sleep(vertx, 2000);
    assertEquals(latch.getCount(), 1);
    runAsync(h -> ctx.writeClient().destroySession(id, h));
    awaitLatch(latch);
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

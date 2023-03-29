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
    writeClient.createSession().onComplete(tc.asyncAssertSuccess(id -> {
      writeClient.infoSession(id).onComplete(tc.asyncAssertSuccess(session -> {
        tc.assertEquals(id, session.getId());
        tc.assertEquals(consul.container.getNodeName(), session.getNode());
        writeClient.destroySession(id).onComplete(tc.asyncAssertSuccess());
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
    writeClient.createSessionWithOptions(opt).onComplete(tc.asyncAssertSuccess(id -> {
      writeClient.infoSession(id).onComplete(tc.asyncAssertSuccess(session -> {
//        List<String> checks = session.getChecks();
//        tc.assertEquals(1, checks.size());
//        tc.assertTrue("serfHealth".equals(checks.get(0)));
        tc.assertEquals(opt.getLockDelay(), session.getLockDelay());
        tc.assertEquals(consul.container.getNodeName(), session.getNode());
        writeClient.destroySession(id).onComplete(tc.asyncAssertSuccess());
      }));
    }));
  }

  @Test
  public void createSessionWithOptionsWithZeroLockDelay(TestContext tc) {
    SessionOptions opt = new SessionOptions()
      .setBehavior(SessionBehavior.DELETE)
      .setLockDelay(0L)
      .setName("optName")
      .setTtl(442);
    writeClient.createSessionWithOptions(opt).onComplete(tc.asyncAssertSuccess(id -> {
      writeClient.infoSession(id).onComplete(tc.asyncAssertSuccess(session -> {
//        List<String> checks = session.getChecks();
//        tc.assertEquals(1, checks.size());
//        tc.assertTrue("serfHealth".equals(checks.get(0)));
        tc.assertEquals(0L, session.getLockDelay());
        tc.assertEquals(consul.container.getNodeName(), session.getNode());
        writeClient.destroySession(id).onComplete(tc.asyncAssertSuccess());
      }));
    }));
  }

  @Test
  public void createSessionWithOptionsWithDefaultLockDelay(TestContext tc) {
    SessionOptions opt = new SessionOptions()
      .setBehavior(SessionBehavior.DELETE)
      .setName("optName")
      .setTtl(442);
    writeClient.createSessionWithOptions(opt).onComplete(tc.asyncAssertSuccess(id -> {
      writeClient.infoSession(id).onComplete(tc.asyncAssertSuccess(session -> {
//        List<String> checks = session.getChecks();
//        tc.assertEquals(1, checks.size());
//        tc.assertTrue("serfHealth".equals(checks.get(0)));
        tc.assertEquals(15L, session.getLockDelay());
        tc.assertEquals(consul.container.getNodeName(), session.getNode());
        writeClient.destroySession(id).onComplete(tc.asyncAssertSuccess());
      }));
    }));
  }

  @Test
  public void unknownNode(TestContext tc) {
    writeClient
      .createSessionWithOptions(new SessionOptions().setNode("unknownNode"))
      .onComplete(tc.asyncAssertFailure());
  }

  @Test
  public void unknownSession(TestContext tc) {
    writeClient.infoSession("00000000-0000-0000-0000-000000000000").onComplete(tc.asyncAssertFailure());
  }

  @Test
  public void listSessions(TestContext tc) {
    writeClient.createSession().onComplete(tc.asyncAssertSuccess(id -> {
      writeClient.infoSession(id).onComplete(tc.asyncAssertSuccess(session -> {
        writeClient.listSessions().onComplete(tc.asyncAssertSuccess(list -> {
          tc.assertEquals(session.getId(), list.getList().get(0).getId());
          writeClient.listNodeSessions(session.getNode()).onComplete(tc.asyncAssertSuccess(nodeSesions -> {
            tc.assertEquals(session.getId(), nodeSesions.getList().get(0).getId());
            writeClient.destroySession(id).onComplete(tc.asyncAssertSuccess());
          }));
        }));
      }));
    }));
  }

  @Test
  public void listSessionsBlocking(TestContext tc) throws InterruptedException {
    testSessionsBlocking(tc, (opts, h) -> readClient.listSessionsWithOptions(opts).onComplete(h));
  }

  @Test
  public void listNodeSessionsBlocking(TestContext tc) throws InterruptedException {
    testSessionsBlocking(tc, (opts, h) ->
      readClient.listNodeSessionsWithOptions(consul.container.getNodeName(), opts).onComplete(h)
    );
  }

  private void testSessionsBlocking(
    TestContext tc,
    BiConsumer<BlockingQueryOptions, Handler<AsyncResult<SessionList>>> request
  ) {
    writeClient.createSession().onComplete(tc.asyncAssertSuccess(id1 -> {
      readClient.listSessions().onComplete(tc.asyncAssertSuccess(list1 -> {
        Async async = tc.async();
        request.accept(new BlockingQueryOptions().setIndex(list1.getIndex()), h -> {
          List<String> ids = h.result().getList().stream().map(Session::getId).collect(Collectors.toList());
          assertTrue(ids.contains(id1));
          async.countDown();
        });
        vertx.setTimer(1000, l -> {
          assertEquals(async.count(), 1);
          writeClient.createSession().onComplete(tc.asyncAssertSuccess(id2 -> {
            async.handler(a -> {
              writeClient.destroySession(id1).onComplete(tc.asyncAssertSuccess(d1 -> {
                writeClient.destroySession(id2).onComplete(tc.asyncAssertSuccess());
              }));
            });
          }));
        });
      }));
    }));
  }

  @Test
  public void sessionInfoBlocking(TestContext tc) {
    writeClient.createSession().onComplete(tc.asyncAssertSuccess(id -> {
      readClient.infoSession(id).onComplete(tc.asyncAssertSuccess(s1 -> {
        Async async = tc.async();
        readClient
          .infoSessionWithOptions(id, new BlockingQueryOptions().setIndex(s1.getIndex()))
          .onComplete(h -> async.countDown());
        vertx.setTimer(1000, l -> {
          assertEquals(async.count(), 1);
          writeClient.destroySession(id).onComplete(tc.asyncAssertSuccess());
        });
      }));
    }));
  }

  @Test
  public void deleteBehavior(TestContext tc) {
    writeClient
      .createSessionWithOptions(new SessionOptions().setTtl(442).setBehavior(SessionBehavior.DELETE))
      .onComplete(tc.asyncAssertSuccess(id -> {
        writeClient
          .putValueWithOptions("foo/bar", "value1", new KeyValueOptions().setAcquireSession(id))
          .onComplete(tc.asyncAssertSuccess(b -> {
            tc.assertTrue(b);
            writeClient.getValue("foo/bar").onComplete(tc.asyncAssertSuccess(pair -> {
              tc.assertEquals("value1", pair.getValue());
              tc.assertEquals(id, pair.getSession());
              writeClient.destroySession(id).onComplete(tc.asyncAssertSuccess(v -> {
                writeClient.getValue("foo/bar").onComplete(tc.asyncAssertSuccess(notfound -> {
                  tc.assertFalse(notfound.isPresent());
                }));
              }));
            }));
          }));
      }));
  }
}

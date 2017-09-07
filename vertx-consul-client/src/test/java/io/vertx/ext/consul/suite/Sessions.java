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
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;
import static io.vertx.ext.consul.Utils.sleep;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Sessions extends ConsulTestBase {

  @Test
  public void sessionOptionsSerialization() {
    SessionOptions options = new SessionOptions()
      .setBehavior(SessionBehavior.RELEASE)
      .setChecks(Arrays.asList("c1", "c2"))
      .setLockDelay(42)
      .setName("optName")
      .setNode("optNode")
      .setTtl(442);
    SessionOptions restored = new SessionOptions(options.toJson());
    assertEquals(options.getBehavior(), restored.getBehavior());
    assertEquals(options.getChecks(), restored.getChecks());
    assertEquals(options.getLockDelay(), restored.getLockDelay());
    assertEquals(options.getName(), restored.getName());
    assertEquals(options.getNode(), restored.getNode());
    assertEquals(options.getTtl(), restored.getTtl());
  }

  @Test
  public void createDefaultSession() {
    String id = getAsync(h -> ctx.writeClient().createSession(h));
    Session session = getAsync(h -> ctx.writeClient().infoSession(id, h));
    assertEquals(id, session.getId());
    assertEquals(ctx.nodeName(), session.getNode());
    runAsync(h -> ctx.writeClient().destroySession(id, h));
  }

  @Test
  public void createSessionWithOptions() {
    SessionOptions opt = new SessionOptions()
      .setBehavior(SessionBehavior.DELETE)
      .setLockDelay(42)
      .setName("optName")
      .setTtl(442);
    String id = getAsync(h -> ctx.writeClient().createSessionWithOptions(opt, h));
    Session session = getAsync(h -> ctx.writeClient().infoSession(id, h));
    List<String> checks = session.getChecks();
    assertEquals(1, checks.size());
    assertTrue("serfHealth".equals(checks.get(0)));
    assertEquals(opt.getLockDelay(), session.getLockDelay());
    assertEquals(ctx.nodeName(), session.getNode());
    runAsync(h -> ctx.writeClient().destroySession(id, h));
  }

  @Test(expected = RuntimeException.class)
  public void unknownNode() {
    Utils.<String>getAsync(h -> ctx.writeClient().createSessionWithOptions(new SessionOptions().setNode("unknownNode"), h));
  }

  @Test(expected = RuntimeException.class)
  public void unknownSession() {
    Utils.<Session>getAsync(h -> ctx.writeClient().infoSession("00000000-0000-0000-0000-000000000000", h));
  }

  @Test
  public void listSessions() {
    String id = getAsync(h -> ctx.writeClient().createSession(h));
    Session session = getAsync(h -> ctx.writeClient().infoSession(id, h));
    SessionList list = getAsync(h -> ctx.writeClient().listSessions(h));
    assertEquals(session.getId(), list.getList().get(0).getId());
    SessionList nodeSesions = getAsync(h -> ctx.writeClient().listNodeSessions(session.getNode(), h));
    assertEquals(session.getId(), nodeSesions.getList().get(0).getId());
    runAsync(h -> ctx.writeClient().destroySession(id, h));
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
  public void deleteBehavior() {
    String id = getAsync(h -> ctx.writeClient().createSessionWithOptions(new SessionOptions().setTtl(442).setBehavior(SessionBehavior.DELETE), h));
    assertTrue(getAsync(h -> ctx.writeClient().putValueWithOptions("foo/bar", "value1", new KeyValueOptions().setAcquireSession(id), h)));
    KeyValue pair = getAsync(h -> ctx.writeClient().getValue("foo/bar", h));
    assertEquals("value1", pair.getValue());
    assertEquals(id, pair.getSession());
    runAsync(h -> ctx.writeClient().destroySession(id, h));
    ctx.writeClient().getValue("foo/bar", h -> {
      if (h.succeeded() && !h.result().isPresent()) {
        testComplete();
      }
    });
    await(10, TimeUnit.SECONDS);
  }
}

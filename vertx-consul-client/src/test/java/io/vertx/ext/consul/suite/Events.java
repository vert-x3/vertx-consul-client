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

import io.vertx.ext.consul.*;
import io.vertx.test.core.TestUtils;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.sleep;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Events extends ConsulTestBase {

  @Test
  public void events() throws InterruptedException {
    runTest(false);
  }

  @Test
  public void timeout() throws InterruptedException {
    runTest(true);
  }

  private void runTest(boolean timeout) throws InterruptedException {
    String name1 = "custom-event-1-" + TestUtils.randomAlphaString(10);
    String name2 = "custom-event-2-" + TestUtils.randomAlphaString(10);
    EventOptions opts = new EventOptions().setPayload("content");
    Event event = getAsync(h -> ctx.writeClient().fireEventWithOptions(name1, opts, h));
    assertEquals(name1, event.getName());
    assertEquals(opts.getPayload(), event.getPayload());
    String evId1 = event.getId();
    EventList list1 = getAsync(h -> ctx.writeClient().listEvents(h));
    long cnt1 = list1.getList().stream().map(Event::getId).filter(id -> id.equals(evId1)).count();
    assertEquals(cnt1, 1);

    EventList list2 = getAsync(h -> ctx.writeClient().listEventsWithOptions(new EventListOptions().setName(name2), h));
    assertEquals(list2.getList().size(), 0);

    CountDownLatch latch = new CountDownLatch(1);
    BlockingQueryOptions blockingQueryOptions = new BlockingQueryOptions()
      .setIndex(list1.getIndex());
    if (timeout) {
      blockingQueryOptions.setWait("2s");
    }
    ctx.writeClient().listEventsWithOptions(new EventListOptions().setBlockingOptions(blockingQueryOptions), h -> {
      List<String> names = h.result().getList().stream().map(Event::getName).collect(Collectors.toList());
      if (timeout) {
        assertTrue(names.contains(name1));
        assertFalse(names.contains(name2));
      } else {
        assertTrue(names.contains(name1));
        assertTrue(names.contains(name2));
      }
      latch.countDown();
    });
    assertEquals(latch.getCount(), 1);
    sleep(vertx, 4000);
    assertEquals(latch.getCount(), timeout ? 0 : 1);
    Utils.<Event>getAsync(h -> ctx.writeClient().fireEvent(name2, h));
    awaitLatch(latch);


    EventList list3 = getAsync(h -> ctx.writeClient().listEventsWithOptions(new EventListOptions().setName(name2), h));
    assertEquals(list3.getList().size(), 1);

  }

}

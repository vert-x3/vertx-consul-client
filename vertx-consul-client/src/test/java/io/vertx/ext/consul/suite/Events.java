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
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;
import static io.vertx.ext.consul.Utils.sleep;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Events extends ConsulTestBase {

  @Test
  public void events() throws InterruptedException {
    String name1 = "custom-event1";
    String name2 = "custom-event2";
    EventOptions opts = new EventOptions().setPayload("content");
    Event event = getAsync(h -> writeClient.fireEventWithOptions(name1, opts, h));
    assertEquals(name1, event.getName());
    assertEquals(opts.getPayload(), event.getPayload());
    String evId1 = event.getId();
    EventList list1 = getAsync(h -> writeClient.listEvents(h));
    long cnt = list1.getList().stream().map(Event::getId).filter(id -> id.equals(evId1)).count();
    assertEquals(cnt, 1);

    CountDownLatch latch = new CountDownLatch(1);
    writeClient.listEventsWithOptions(new BlockingQueryOptions().setIndex(list1.getIndex()), h -> {
      List<String> names = h.result().getList().stream().map(Event::getName).collect(Collectors.toList());
      assertTrue(names.contains(name2));
      latch.countDown();
    });
    sleep(vertx, 2000);
    assertEquals(latch.getCount(), 1);
    Utils.<Event>getAsync(h -> writeClient.fireEvent(name2, h));
    awaitLatch(latch);

  }

}

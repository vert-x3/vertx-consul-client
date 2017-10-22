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
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

import static io.vertx.test.core.TestUtils.randomAlphaString;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class Events extends ConsulTestBase {

  @Test
  public void events(TestContext tc) {
    runTest(tc, false);
  }

  @Test
  public void timeout(TestContext tc) {
    runTest(tc, true);
  }

  private void runTest(TestContext tc, boolean timeout) {
    String name1 = randomAlphaString(10);
    String name2 = randomAlphaString(10);
    EventOptions opts = new EventOptions().setPayload(randomAlphaString(10));
    ctx.writeClient().fireEventWithOptions(name1, opts, tc.asyncAssertSuccess(event -> {
      tc.assertEquals(name1, event.getName());
      tc.assertEquals(opts.getPayload(), event.getPayload());
      String evId1 = event.getId();
      ctx.writeClient().listEvents(tc.asyncAssertSuccess(list1 -> {
        long cnt1 = list1.getList().stream()
          .map(Event::getId)
          .filter(id -> id.equals(evId1))
          .count();
        tc.assertEquals(cnt1, (long) 1);
        ctx.writeClient().listEventsWithOptions(new EventListOptions().setName(name2), tc.asyncAssertSuccess(list2 -> {
          tc.assertEquals(list2.getList().size(), 0);
          Async async = tc.async(2);
          BlockingQueryOptions blockingQueryOptions = new BlockingQueryOptions()
            .setIndex(list1.getIndex());
          if (timeout) {
            blockingQueryOptions.setWait("2s");
          }
          ctx.writeClient().listEventsWithOptions(new EventListOptions().setBlockingOptions(blockingQueryOptions), tc.asyncAssertSuccess(h -> {
            List<String> names = h.getList().stream().map(Event::getName).collect(Collectors.toList());
            if (timeout) {
              tc.assertTrue(names.contains(name1));
              tc.assertFalse(names.contains(name2));
            } else {
              tc.assertTrue(names.contains(name1));
              tc.assertTrue(names.contains(name2));
            }
            async.countDown();
          }));
          tc.assertEquals(async.count(), 2);
          vertx.setTimer(4000, l -> {
            tc.assertEquals(async.count(), timeout ? 1 : 2);
            ctx.writeClient().fireEvent(name2, tc.asyncAssertSuccess(ev -> {
              ctx.writeClient().listEventsWithOptions(new EventListOptions().setName(name2), tc.asyncAssertSuccess(list3 -> {
                tc.assertEquals(list3.getList().size(), 1);
                async.complete();
              }));
            }));
          });
        }));
      }));
    }));
  }
}

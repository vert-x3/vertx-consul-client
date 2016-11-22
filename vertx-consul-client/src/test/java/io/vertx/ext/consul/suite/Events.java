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
import io.vertx.ext.consul.Event;
import io.vertx.ext.consul.EventOptions;
import org.junit.Test;

import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Events extends ConsulTestBase {

  @Test
  public void testEvents1() {
    String name = "custom-event";
    EventOptions opts = new EventOptions().setPayload("content");
    Event event = getAsync(h -> writeClient.fireEventWithOptions(name, opts, h));
    assertEquals(name, event.getName());
    assertEquals(opts.getPayload(), event.getPayload());
    String evId = event.getId();
    List<Event> list = getAsync(h -> writeClient.listEvents(h));
    long cnt = list.stream().map(Event::getId).filter(id -> id.equals(evId)).count();
    assertEquals(cnt, 1);
  }

}

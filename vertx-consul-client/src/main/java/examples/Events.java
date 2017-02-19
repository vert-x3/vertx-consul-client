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
package examples;

import io.vertx.ext.consul.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Events {

  public void sendWithName(ConsulClient consulClient) {

    consulClient.fireEvent("eventName", res -> {
      if (res.succeeded()) {
        System.out.println("Event sent");
        System.out.println("id: " + res.result().getId());
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void sendWithOptions(ConsulClient consulClient) {

    EventOptions opts = new EventOptions()
      .setTag("tag")
      .setPayload("message");

    consulClient.fireEventWithOptions("eventName", opts, res -> {
      if (res.succeeded()) {
        System.out.println("Event sent");
        System.out.println("id: " + res.result().getId());
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void list(ConsulClient consulClient) {

    consulClient.listEvents(res -> {
      if (res.succeeded()) {
        System.out.println("Consul index: " + res.result().getIndex());
        for(Event event: res.result().getList()) {
          System.out.println("Event id: " + event.getId());
          System.out.println("Event name: " + event.getName());
          System.out.println("Event payload: " + event.getPayload());
        }
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void listWithOptions(ConsulClient consulClient, long lastIndex) {

    EventListOptions opts = new EventListOptions()
      .setName("eventName")
      .setBlockingOptions(new BlockingQueryOptions().setIndex(lastIndex));

    consulClient.listEventsWithOptions(opts, res -> {
      if (res.succeeded()) {
        System.out.println("Consul index: " + res.result().getIndex());
        for(Event event: res.result().getList()) {
          System.out.println("Event id: " + event.getId());
        }
      } else {
        res.cause().printStackTrace();
      }
    });

  }

}

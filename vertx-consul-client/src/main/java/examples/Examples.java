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

import io.vertx.core.Vertx;
import io.vertx.ext.consul.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Examples {

  public void exampleCreateDefault(Vertx vertx) {

    ConsulClient client = ConsulClient.create(vertx);

  }

  public void exampleCreateWithOptions(Vertx vertx) {

    ConsulClientOptions options = new ConsulClientOptions()
      .setHost("consul.example.com");

    ConsulClient client = ConsulClient.create(vertx, options);

  }

  public void blockingOptions(long lastIndex) {

    BlockingQueryOptions opts = new BlockingQueryOptions()
      .setIndex(lastIndex)
      .setWait("1m");

  }

  public void events(ConsulClient consulClient) {

    EventOptions opts = new EventOptions()
      .setTag("tag")
      .setPayload("message");

    // trigger a new user event

    consulClient.fireEventWithOptions("eventName", opts, res -> {

      if (res.succeeded()) {

        System.out.println("Event sent");

        System.out.println("id: " + res.result().getId());

      } else {

        res.cause().printStackTrace();

      }

    });

    // most recent events known by the agent

    consulClient.listEvents(res -> {

      if (res.succeeded()) {

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

  public void sessions(ConsulClient consulClient, String sessionId, long lastIndex) {

    SessionOptions opts = new SessionOptions()
      .setNode("nodeId")
      .setBehavior(SessionBehavior.RELEASE);

    // Create session

    consulClient.createSessionWithOptions(opts, res -> {

      if (res.succeeded()) {

        System.out.println("Session successfully created");

        System.out.println("id: " + res.result());

      } else {

        res.cause().printStackTrace();

      }

    });

    // Lists sessions belonging to a node

    consulClient.listNodeSessions("nodeId", res -> {

      if (res.succeeded()) {

        for(Session session: res.result().getList()) {

          System.out.println("Session id: " + session.getId());

          System.out.println("Session node: " + session.getNode());

          System.out.println("Session create index: " + session.getCreateIndex());

        }

      } else {

        res.cause().printStackTrace();

      }

    });

    // Blocking query for all active sessions

    BlockingQueryOptions blockingOpts = new BlockingQueryOptions()
      .setIndex(lastIndex);

    consulClient.listSessionsWithOptions(blockingOpts, res -> {

      if (res.succeeded()) {

        System.out.println("Found " + res.result().getList().size() + " sessions");

      } else {

        res.cause().printStackTrace();

      }

    });

    // Destroy session

    consulClient.destroySession(sessionId, res -> {

      if (res.succeeded()) {

        System.out.println("Session successfully destroyed");

      } else {

        res.cause().printStackTrace();

      }

    });

  }

  public void nodes(ConsulClient consulClient, long lastIndex) {

    consulClient.catalogNodes(res -> {

      if (res.succeeded()) {

        System.out.println("found " + res.result().getList().size() + " nodes");

        System.out.println("consul state index " + res.result().getIndex());

      } else {

        res.cause().printStackTrace();

      }

    });

    // blocking request to catalog for nodes, sorted by distance from agent

    NodeQueryOptions opts = new NodeQueryOptions()
      .setNear("_agent")
      .setBlockingOptions(new BlockingQueryOptions().setIndex(lastIndex));

    consulClient.catalogNodesWithOptions(opts, res -> {

      if (res.succeeded()) {

        System.out.println("found " + res.result().getList().size() + " nodes");

      } else {

        res.cause().printStackTrace();

      }

    });

  }

}

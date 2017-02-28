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
public class Sessions {

  public void sessionOpts() {

    SessionOptions opts = new SessionOptions()
      .setNode("nodeId")
      .setBehavior(SessionBehavior.RELEASE);

  }

  public void create(ConsulClient consulClient, SessionOptions opts) {

    consulClient.createSessionWithOptions(opts, res -> {
      if (res.succeeded()) {
        System.out.println("Session successfully created");
        System.out.println("id: " + res.result());
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void destroy(ConsulClient consulClient, String sessionId) {

    consulClient.destroySession(sessionId, res -> {
      if (res.succeeded()) {
        System.out.println("Session successfully destroyed");
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void nodeSessions(ConsulClient consulClient) {

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

  }

  public void blockingQuery(ConsulClient consulClient, long lastIndex) {

    BlockingQueryOptions blockingOpts = new BlockingQueryOptions()
      .setIndex(lastIndex);

    consulClient.listSessionsWithOptions(blockingOpts, res -> {
      if (res.succeeded()) {
        System.out.println("Found " + res.result().getList().size() + " sessions");
      } else {
        res.cause().printStackTrace();
      }
    });

  }
}

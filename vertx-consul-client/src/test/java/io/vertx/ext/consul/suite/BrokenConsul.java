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

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Utils;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class BrokenConsul extends ConsulTestBase {

  @Test
  public void closedConnection() {
    BrokenHttpServer brokenConsul = new BrokenHttpServer(vertx);
    ConsulClient client = clientCreator.apply(vertx, new JsonObject().put("port", brokenConsul.port()));
    client.agentInfo(h -> {
      if (h.failed() && h.cause().getMessage().contains("Connection was closed")) {
        testComplete();
      }
    });
    await();
    clientCloser.accept(client);
    brokenConsul.close();
  }

  static class BrokenHttpServer {

    private final HttpServer server;
    private final int port;

    BrokenHttpServer(Vertx vertx) {
      this.port = Utils.getFreePort();
      CountDownLatch latch = new CountDownLatch(1);
      this.server = vertx.createHttpServer()
        .requestHandler(h -> h.response().putHeader(HttpHeaders.CONTENT_LENGTH, "10000").write("start and ... ").close())
        .listen(port, h -> latch.countDown());
      try {
        latch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    int port() {
      return port;
    }

    void close() {
      server.close();
    }
  }
}

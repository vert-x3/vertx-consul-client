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

import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Function;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class BrokenClient extends ConsulTestBase {

  @Test
  public void unknownHost() {
    ConsulClient unknownHost = clientCreator.apply(vertx, new JsonObject().put("host", "unknownConsulHost"));
    tryClient(unknownHost, message -> message.contains("unknownConsulHost"));
  }

  @Test
  public void unknownPort() {
    ConsulClient unknownPort = clientCreator.apply(vertx, new JsonObject().put("port", getUnusedPort()));
    tryClient(unknownPort, message -> message.contains("Connection refused"));
  }

  private void tryClient(ConsulClient client, Function<String, Boolean> expectedExceptionMessage) {
    client.agentInfo(h -> {
      if (h.failed() && expectedExceptionMessage.apply(h.cause().getMessage())) {
        testComplete();
      }
    });
    await();
    clientCloser.accept(client);
  }

  private static int getUnusedPort() {
    int port = 0;
    try {
      ServerSocket s = new ServerSocket(0);
      port = s.getLocalPort();
      s.close();
    } catch (IOException ignore) {
    }
    return port;
  }
}

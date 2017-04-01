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

import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Utils;
import org.junit.Test;

import java.util.function.Function;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class BrokenClient extends ConsulTestBase {

  @Test
  public void unknownHost() {
    ConsulClient unknownHost = ctx.createClient(new ConsulClientOptions().setHost("unknownConsulHost"));
    tryClient(unknownHost, message -> message.contains("unknownConsulHost"));
  }

  @Test
  public void unknownPort() {
    ConsulClient unknownPort = ctx.createClient(new ConsulClientOptions().setPort(Utils.getFreePort()));
    tryClient(unknownPort, message -> message.contains("Connection refused"));
  }

  private void tryClient(ConsulClient client, Function<String, Boolean> expectedExceptionMessage) {
    client.agentInfo(h -> {
      if (h.failed() && expectedExceptionMessage.apply(h.cause().getMessage())) {
        testComplete();
      }
    });
    await();
    ctx.closeClient(client);
  }

}

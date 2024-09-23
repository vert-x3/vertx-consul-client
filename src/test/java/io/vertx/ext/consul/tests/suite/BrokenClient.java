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
package io.vertx.ext.consul.tests.suite;

import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.tests.ConsulTestBase;
import io.vertx.ext.consul.tests.Utils;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class BrokenClient extends ConsulTestBase {

  @Test
  public void unknownHost(TestContext tc) {
    ConsulClient unknownHost = consul.createClient(vertx, new ConsulClientOptions().setHost("unknownConsulHost"));
    tryClient(tc, unknownHost, "unknownConsulHost");
  }

  @Test
  public void unknownPort(TestContext tc) {
    ConsulClient unknownPort = consul.createClient(vertx, new ConsulClientOptions().setPort(Utils.getFreePort()));
    tryClient(tc, unknownPort, "Connection refused");
  }

  private void tryClient(TestContext tc, ConsulClient client, String expectedExceptionMessageSubstring) {
    client.agentInfo().onComplete(tc.asyncAssertFailure(t -> {
      tc.assertTrue(t.getMessage().contains(expectedExceptionMessageSubstring));
      client.close();
    }));
  }
}

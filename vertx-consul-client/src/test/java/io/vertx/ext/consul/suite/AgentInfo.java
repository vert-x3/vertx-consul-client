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
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import static io.vertx.ext.consul.Utils.getAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class AgentInfo extends ConsulTestBase {

  @Test
  public void info() {
    JsonObject info = getAsync(h -> ctx.readClient().agentInfo(h));
    JsonObject config = info.getJsonObject("Config");
    assertEquals(config.getString("Datacenter"), ctx.dc().getName());
  }
}

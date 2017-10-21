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
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class Status extends ConsulTestBase {

  @Test
  public void leader(TestContext tc) {
    ctx.readClient().leaderStatus(tc.asyncAssertSuccess(leader -> {
      tc.assertEquals(leader.substring(0, leader.indexOf(':')), "127.0.0.1");
    }));
  }

  @Test
  public void peers(TestContext tc) {
    ctx.readClient().peersStatus(tc.asyncAssertSuccess(peers -> {
      tc.assertEquals(peers.size(), 1);
    }));
  }

}

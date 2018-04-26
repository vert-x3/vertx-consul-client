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
package io.vertx.ext.consul.common;

import io.vertx.core.Vertx;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class StateConsumerTest {

  @Test
  public void test1() throws InterruptedException {
    final Vertx vertx = Vertx.vertx();
    final StateConsumer<Integer> consumer = new StateConsumer<>(false);
    final CountDownLatch latch = new CountDownLatch(1);
    final int n = 100000;
    vertx.runOnContext(v -> {
      latch.countDown();
      for (int i = 0; i < n; i++) {
        consumer.consume(i);
      }
    });
    latch.await();
    for (int i = 0; i < n; i++) {
      consumer.await(i);
    }
    consumer.check();
    vertx.close();
  }
}

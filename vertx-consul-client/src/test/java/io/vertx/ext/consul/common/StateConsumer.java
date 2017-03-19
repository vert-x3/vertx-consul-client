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

import io.vertx.core.Context;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class StateConsumer<T> {

  private final List<T> consumed = new ArrayList<>();
  private final Deque<T> deque = new ConcurrentLinkedDeque<>();
  private final long start;
  private final boolean logEnabled;
  private CountDownLatch latch = new CountDownLatch(0);

  public StateConsumer() {
    this(true);
  }

  public StateConsumer(boolean logEnabled) {
    this.start = System.currentTimeMillis();
    this.logEnabled = logEnabled;
  }

  public StateConsumer<T> awaitAny() throws InterruptedException {
    return await(null);
  }

  public StateConsumer<T> await(T state) throws InterruptedException {
    if (Context.isOnVertxThread()) {
      throw new RuntimeException("wait() shouldn't be called in Vertx thread");
    }
    return await(state, 10);
  }

  private StateConsumer<T> await(T state, int countDown) throws InterruptedException {
    log("await " + (state == null ? "any" : "'" + state + "'"));
    if (deque.isEmpty()) {
      latch = new CountDownLatch(1);
      latch.await(2, TimeUnit.MINUTES);
    }
    T top = deque.pollFirst();
    if (state != null && !state.equals(top)) {
      if (countDown == 0) {
        throw new RuntimeException("");
      } else {
        await(state, countDown - 1);
      }
    }
    return this;
  }

  public StateConsumer<T> consume(T state) {
    if (!Context.isOnVertxThread()) {
      throw new RuntimeException("consume() should be called in Vertx thread");
    }
    log("consume '" + state + "'");
    consumed.add(state);
    deque.addLast(state);
    latch.countDown();
    return this;
  }

  public StateConsumer<T> check() {
    if (Context.isOnVertxThread()) {
      throw new RuntimeException("check() shouldn't be called in Vertx thread");
    }
    if (!deque.isEmpty()) {
      throw new RuntimeException("expected deque is not empty");
    }
    return this;
  }

  public List<T> getConsumed() {
    return consumed;
  }

  private void log(String message) {
    if (logEnabled) {
      System.out.println((System.currentTimeMillis() - start) + " ms: " + message);
    }
  }
}

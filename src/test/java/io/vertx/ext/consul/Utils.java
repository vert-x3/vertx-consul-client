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
package io.vertx.ext.consul;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Utils {

  public static String readResource(String fileName) throws Exception {
    final InputStream is = Utils.class.getClassLoader().getResourceAsStream(fileName);
    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
      return buffer.lines().collect(Collectors.joining("\n"));
    }
  }

  /**
   * A critical note is that the return of a blocking request is no guarantee of a change.
   * It is possible that the timeout was reached or that there was an idempotent write that does not affect
   * the result of the query.
   *
   * @param latch will be count down if success
   * @param maxAttempts maxinum attemps number
   * @param startIndex consul index
   * @param runner blocking query parameters consumer. First parameter is consul index,
   *               second one is the future to collect next consul index for next try if query will be unsuccessful
   */
  public static void waitBlockingQuery(CountDownLatch latch, int maxAttempts, long startIndex, BiConsumer<Long, Future<Long>> runner) {
    if (maxAttempts > 0) {
      Future<Long> future = Future.future();
      runner.accept(startIndex, future);
      future.setHandler(h -> {
        if (h.succeeded()) {
          long nextIndex = h.result();
          if (nextIndex >= 0) {
            int att = maxAttempts - 1;
            System.out.println("... rounds left: " + att);
            waitBlockingQuery(latch, att, nextIndex, runner);
          } else {
            latch.countDown();
          }
        }
      });
    }
  }

  /**
   * Completes future with next consul index in case of unsuccessful result, -1 else.
   * See {@link #waitBlockingQuery(CountDownLatch, int, long, BiConsumer)}
   */
  public static void waitComplete(Vertx vertx, Future<Long> fut, long idx, boolean result) {
    if (result) {
      fut.complete(-1L);
    } else {
      sleep(vertx, 1000);
      fut.complete(idx);
    }
  }

  public static void runAsync(Consumer<Handler<AsyncResult<Void>>> runner) {
    CountDownLatch latch = new CountDownLatch(1);
    Future<Void> future = Future.future();
    runner.accept(h -> {
      if (h.succeeded()) {
        future.complete();
      } else {
        future.fail(h.cause());
      }
      latch.countDown();
    });
    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (future.failed()) {
      throw new RuntimeException(future.cause());
    }
  }

  public static <T> T getAsync(Consumer<Handler<AsyncResult<T>>> runner) {
    CountDownLatch latch = new CountDownLatch(1);
    Future<T> future = Future.future();
    runner.accept(h -> {
      if (h.succeeded()) {
        future.complete(h.result());
      } else {
        future.fail(h.cause());
      }
      latch.countDown();
    });
    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (future.succeeded()) {
      return future.result();
    } else {
      throw new RuntimeException(future.cause());
    }
  }

  public static void sleep(Vertx vertx, long millis) {
    CountDownLatch latch = new CountDownLatch(1);
    vertx.setTimer(millis, h -> latch.countDown());
    try {
      latch.await(2 * millis, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static String osResolve() {
    String os = System.getProperty("os.name").toLowerCase();
    String binaryVersion = "linux";
    if (os.contains("mac")) {
      binaryVersion = "darwin";
    } else if (os.contains("windows")) {
      binaryVersion = "windows";
    }
    return binaryVersion;
  }

  public static boolean isWindows() {
    return "windows".equals(osResolve());
  }

  public static int getFreePort() {
    int port = -1;
    try {
      ServerSocket socket = new ServerSocket(0);
      port = socket.getLocalPort();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return port;
  }
}

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

import io.vertx.core.Vertx;
import io.vertx.ext.consul.impl.ConsulClientImpl;
import io.vertx.test.core.VertxTestBase;
import org.junit.BeforeClass;
import rx.functions.Func1;

import java.util.function.Consumer;
import java.util.function.Function;

import static io.vertx.test.core.TestUtils.randomAlphaString;
import static io.vertx.test.core.TestUtils.randomUnicodeString;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulTestBase extends VertxTestBase {

  static Function<Vertx, ConsulContext> ctxFactory = vertx ->
    new ConsulContext(
      opts -> new ConsulClientImpl(vertx, opts),
      ConsulClient::close
    );
  protected ConsulContext ctx;

  @BeforeClass
  public static void startConsul() throws Exception {
    ConsulCluster.start();
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    ctx = ctxFactory.apply(vertx);
    ctx.start();
  }

  @Override
  public void tearDown() throws Exception {
    ctx.stop();
    ctx = null;
    super.tearDown();
  }

  public static <T> Func1<T, T> check(Consumer<T> check) {
    return s -> {
      check.accept(s);
      return s;
    };
  }

  public static String randomFooBarAlpha() {
    return "foo/bar" + randomAlphaString(10);
  }

  public static String randomFooBarUnicode() {
    return "foo/bar" + randomUnicodeString(10);
  }
}

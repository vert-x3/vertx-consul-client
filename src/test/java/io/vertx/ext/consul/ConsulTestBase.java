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

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vertx.test.core.TestUtils.randomAlphaString;

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

  public static String randomFooBarAlpha() {
    return "foo/bar" + randomAlphaString(10);
  }

  public static String randomFooBarUnicode() {
    StringBuilder builder = new StringBuilder("foo/bar");
    for (int i = 0; i < 10; i++) {
      int c;
      do {
        c = (int) (0xFFFF * Math.random());
      } while (!PRINTABLE_TYPES.contains(Character.getType(c)));
      builder.append((char) c);
    }
    return builder.toString();
  }

  // [closest] subset of golang unicode.isPrint() collection
  private static final Set<Integer> PRINTABLE_TYPES = Stream.of(
    // General category "Cn" in the Unicode specification.
    // Character.UNASSIGNED,
    // General category "Lu" in the Unicode specification.
    Character.UPPERCASE_LETTER,
    // General category "Ll" in the Unicode specification.
    Character.LOWERCASE_LETTER,
    // General category "Lt" in the Unicode specification.
    Character.TITLECASE_LETTER,
    // General category "Lm" in the Unicode specification.
    Character.MODIFIER_LETTER,
    // General category "Lo" in the Unicode specification.
    Character.OTHER_LETTER,
    // General category "Mn" in the Unicode specification.
    Character.NON_SPACING_MARK,
    // General category "Me" in the Unicode specification.
    Character.ENCLOSING_MARK,
    // General category "Mc" in the Unicode specification.
    Character.COMBINING_SPACING_MARK,
    // General category "Nd" in the Unicode specification.
    Character.DECIMAL_DIGIT_NUMBER,
    // General category "Nl" in the Unicode specification.
    Character.LETTER_NUMBER,
    // General category "No" in the Unicode specification.
    Character.OTHER_NUMBER,
    // General category "Zs" in the Unicode specification.
    // Character.SPACE_SEPARATOR,
    // General category "Zl" in the Unicode specification.
    // Character.LINE_SEPARATOR,
    // General category "Zp" in the Unicode specification.
    // Character.PARAGRAPH_SEPARATOR,
    // General category "Cc" in the Unicode specification.
    // Character.CONTROL,
    // General category "Cf" in the Unicode specification.
    // Character.FORMAT,
    // General category "Co" in the Unicode specification.
    // Character.PRIVATE_USE,
    // General category "Cs" in the Unicode specification.
    // Character.SURROGATE,
    // General category "Pd" in the Unicode specification.
    Character.DASH_PUNCTUATION,
    // General category "Ps" in the Unicode specification.
    Character.START_PUNCTUATION,
    // General category "Pe" in the Unicode specification.
    Character.END_PUNCTUATION,
    // General category "Pc" in the Unicode specification.
    Character.CONNECTOR_PUNCTUATION,
    // General category "Po" in the Unicode specification.
    Character.OTHER_PUNCTUATION,
    // General category "Sm" in the Unicode specification.
    Character.MATH_SYMBOL,
    // General category "Sc" in the Unicode specification.
    Character.CURRENCY_SYMBOL,
    // General category "Sk" in the Unicode specification.
    Character.MODIFIER_SYMBOL,
    // General category "So" in the Unicode specification.
    Character.OTHER_SYMBOL,
    // General category "Pi" in the Unicode specification.
    Character.INITIAL_QUOTE_PUNCTUATION,
    // General category "Pf" in the Unicode specification.
    Character.FINAL_QUOTE_PUNCTUATION
  ).map(Integer::valueOf).collect(Collectors.toSet());

}

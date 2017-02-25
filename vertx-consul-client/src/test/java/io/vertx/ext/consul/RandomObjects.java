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

import java.util.ArrayList;
import java.util.List;

import static io.vertx.test.core.TestUtils.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class RandomObjects {

  public static KeyValue randomKeyValue() {
    return new KeyValue()
      .setKey(randomAlphaString(10))
      .setValue(randomAlphaString(10))
      .setSession(randomAlphaString(10))
      .setCreateIndex(randomLong())
      .setFlags(randomLong())
      .setModifyIndex(randomLong())
      .setLockIndex(randomLong());
  }

  public static Service randomService() {
    List<String> tags = new ArrayList<>();
    tags.add(randomAlphaString(10));
    tags.add(randomAlphaString(10));
    return new Service()
      .setNode(randomAlphaString(10))
      .setNodeAddress(randomAlphaString(10))
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10))
      .setAddress(randomAlphaString(10))
      .setPort(randomInt())
      .setTags(tags);
  }

  public static Check randomCheck() {
    int id = randomPositiveInt() % CheckStatus.values().length;
    return new Check()
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10))
      .setNodeName(randomAlphaString(10))
      .setNotes(randomAlphaString(100))
      .setOutput(randomAlphaString(100))
      .setServiceId(randomAlphaString(10))
      .setServiceName(randomAlphaString(10))
      .setStatus(CheckStatus.values()[id]);
  }

  public static Node randomNode() {
    return new Node()
      .setName(randomAlphaString(10))
      .setLanAddress(randomAlphaString(10))
      .setWanAddress(randomAlphaString(10))
      .setAddress(randomAlphaString(10));
  }

  public static Event randomEvent() {
    return new Event()
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10))
      .setPayload(randomAlphaString(100))
      .setNode(randomAlphaString(10))
      .setService(randomAlphaString(10))
      .setTag(randomAlphaString(10))
      .setVersion(randomPositiveInt())
      .setLTime(randomPositiveInt());
  }

  public static ServiceEntry randomServiceEntry() {
    List<Check> checks = new ArrayList<>();
    checks.add(randomCheck());
    checks.add(randomCheck());
    return new ServiceEntry()
      .setNode(randomNode())
      .setService(randomService())
      .setChecks(checks);
  }

  public static Coordinate randomCoordinate() {
    List<Float> vec = new ArrayList<>();
    vec.add(randomFloat());
    vec.add(randomFloat());
    vec.add(randomFloat());
    vec.add(randomFloat());
    vec.add(randomFloat());
    vec.add(randomFloat());
    return new Coordinate()
      .setNode(randomAlphaString(10))
      .setAdj(randomFloat())
      .setHeight(randomFloat())
      .setErr(randomFloat())
      .setVec(vec);
  }

  public static Session randomSession() {
    List<String> checks = new ArrayList<>();
    checks.add(randomAlphaString(10));
    checks.add(randomAlphaString(10));
    return new Session()
      .setNode(randomAlphaString(10))
      .setId(randomAlphaString(10))
      .setLockDelay(randomLong())
      .setCreateIndex(randomLong())
      .setIndex(randomLong())
      .setChecks(checks);
  }
}

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

import io.vertx.test.core.TestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static io.vertx.test.core.TestUtils.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class RandomObjects {

  public static AclToken randomAclToken() {
    return new AclToken()
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10))
      .setType(randomElement(AclTokenType.values()))
      .setRules(randomAlphaString(10));
  }

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
    return new Service()
      .setNode(randomAlphaString(10))
      .setNodeAddress(randomAlphaString(10))
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10))
      .setAddress(randomAlphaString(10))
      .setPort(randomInt())
      .setTags(randomStringList(2));
  }

  public static Check randomCheck() {
    return new Check()
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10))
      .setNodeName(randomAlphaString(10))
      .setNotes(randomAlphaString(100))
      .setOutput(randomAlphaString(100))
      .setServiceId(randomAlphaString(10))
      .setServiceName(randomAlphaString(10))
      .setStatus(randomElement(CheckStatus.values()));
  }

  public static CheckOptions randomCheckOptions() {
    return new CheckOptions()
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10))
      .setNotes(randomAlphaString(10))
      .setServiceId(randomAlphaString(10))
      .setStatus(randomElement(CheckStatus.values()))
      .setTtl(randomSeconds());
  }

  private static String randomSeconds() {
    return (1 + (randomPositiveInt() % 10)) + "s";
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

  public static PreparedQueryDefinition randomPreparedQueryDefinition() {
    return new PreparedQueryDefinition()
      .setDcs(randomStringList(1))
      .setDnsTtl(randomSeconds())
      .setId(randomAlphaString(10))
      .setMeta(Collections.singletonMap(randomAlphaString(5), randomAlphaString(10)))
      .setName(randomAlphaString(10))
      .setNearestN(randomPositiveInt())
      .setPassing(randomBoolean())
      .setService(randomAlphaString(10))
      .setSession(randomAlphaString(10))
      .setTags(randomStringList(2))
      .setTemplateRegexp(randomAlphaString(10))
      .setTemplateType(randomAlphaString(10))
      .setToken(randomAlphaString(10));
  }

  public static ServiceEntry randomServiceEntry() {
    return new ServiceEntry()
      .setNode(randomNode())
      .setService(randomService())
      .setChecks(randomObjectList(2, RandomObjects::randomCheck));
  }

  public static Coordinate randomCoordinate() {
    return new Coordinate()
      .setNode(randomAlphaString(10))
      .setAdj(randomFloat())
      .setHeight(randomFloat())
      .setErr(randomFloat())
      .setVec(randomObjectList(6, TestUtils::randomFloat));
  }

  public static Session randomSession() {
    return new Session()
      .setNode(randomAlphaString(10))
      .setId(randomAlphaString(10))
      .setLockDelay(randomLong())
      .setCreateIndex(randomLong())
      .setIndex(randomLong())
      .setChecks(randomStringList(2));
  }

  public static SessionOptions randomSessionOptions() {
    return new SessionOptions()
      .setBehavior(randomElement(SessionBehavior.values()))
      .setChecks(randomStringList(2))
      .setLockDelay(randomPositiveInt())
      .setName(randomAlphaString(10))
      .setNode(randomAlphaString(10))
      .setTtl(10 + (randomPositiveInt() % 100));
  }

  public static ServiceOptions randomServiceOptions() {
    return new ServiceOptions()
      .setId(randomAlphaString(10))
      .setName(randomAlphaString(10))
      .setTags(randomStringList(2))
      .setCheckOptions(randomCheckOptions())
      .setAddress(randomAlphaString(10))
      .setPort(randomPortInt());
  }

  public static List<String> randomStringList(int n) {
    return randomObjectList(n, () -> randomAlphaString(10));
  }

  public static <T> List<T> randomObjectList(int n, Supplier<T> s) {
    List<T> list = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      list.add(s.get());
    }
    return list;
  }
}

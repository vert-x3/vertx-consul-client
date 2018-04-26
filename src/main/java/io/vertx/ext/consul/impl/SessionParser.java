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
package io.vertx.ext.consul.impl;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.Session;

import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.impl.Utils.listOf;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class SessionParser {

  private static final String LOCK_KEY = "LockDelay";
  private static final String NODE_KEY = "Node";
  private static final String CHECKS_KEY = "Checks";
  private static final String ID_KEY = "ID";
  private static final String CREATE_INDEX_KEY = "CreateIndex";

  static Session parse(JsonObject session) {
    return parse(session, 0);
  }

  static Session parse(JsonObject session, long index) {
    Session res = new Session();
    res.setLockDelay(TimeUnit.NANOSECONDS.toSeconds(session.getLong(LOCK_KEY, 0L)));
    res.setNode(session.getString(NODE_KEY));
    res.setId(session.getString(ID_KEY));
    res.setCreateIndex(session.getLong(CREATE_INDEX_KEY, 0L));
    JsonArray arr = session.getJsonArray(CHECKS_KEY);
    res.setChecks(listOf(arr));
    res.setIndex(index);
    return res;
  }
}

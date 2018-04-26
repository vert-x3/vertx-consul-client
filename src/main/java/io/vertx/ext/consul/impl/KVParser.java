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

import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.KeyValue;

import java.util.Base64;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class KVParser {

  private static final String KEY_KEY = "Key";
  private static final String VALUE_KEY = "Value";
  private static final String SESSION_KEY = "Session";
  private static final String FLAGS_KEY = "Flags";
  private static final String CREATE_KEY = "CreateIndex";
  private static final String MODIFY_KEY = "ModifyIndex";
  private static final String LOCK_KEY = "LockIndex";

  static KeyValue parse(JsonObject json) {
    return new KeyValue()
      .setKey(json.getString(KEY_KEY))
      .setValue(Utils.decode64(json.getString(VALUE_KEY)))
      .setSession(json.getString(SESSION_KEY))
      .setFlags(json.getLong(FLAGS_KEY, 0L))
      .setCreateIndex(json.getLong(CREATE_KEY, 0L))
      .setModifyIndex(json.getLong(MODIFY_KEY, 0L))
      .setLockIndex(json.getLong(LOCK_KEY, 0L));
  }
}

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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Represents key/value pair stored in Consul
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/agent/http/kv.html">Consul key/value store</a>
 */
@DataObject
public class KeyValue {

  private static final String KEY_KEY = "Key";
  private static final String VALUE_KEY = "Value";
  private static final String SESSION_KEY = "Session";
  private static final String FLAGS_KEY = "Flags";
  private static final String CREATE_KEY = "CreateIndex";
  private static final String MODIFY_KEY = "ModifyIndex";
  private static final String LOCK_KEY = "LockIndex";

  private String key;
  private String value;
  private String session;
  private long flags;
  private long createIndex;
  private long modifyIndex;
  private long lockIndex;

  /**
   * Default constructor
   */
  public KeyValue() {}

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public KeyValue(JsonObject json) {
    key = json.getString(KEY_KEY);
    value = json.getString(VALUE_KEY);
    session = json.getString(SESSION_KEY);
    flags = json.getLong(FLAGS_KEY, 0L);
    createIndex = json.getLong(CREATE_KEY, 0L);
    modifyIndex = json.getLong(MODIFY_KEY, 0L);
    lockIndex = json.getLong(LOCK_KEY, 0L);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (key != null) {
      jsonObject.put(KEY_KEY, key);
    }
    if (value != null) {
      jsonObject.put(VALUE_KEY, value);
    }
    if (session != null) {
      jsonObject.put(SESSION_KEY, session);
    }
    if (flags != 0) {
      jsonObject.put(FLAGS_KEY, flags);
    }
    if (createIndex != 0) {
      jsonObject.put(CREATE_KEY, createIndex);
    }
    if (modifyIndex != 0) {
      jsonObject.put(MODIFY_KEY, modifyIndex);
    }
    if (lockIndex != 0) {
      jsonObject.put(LOCK_KEY, lockIndex);
    }
    return jsonObject;
  }

  /**
   * Get the key
   *
   * @return the key
   */
  public String getKey() {
    return key;
  }

  /**
   * Get the value
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * Get the session that owns the lock
   *
   * @return the session that owns the lock
   */
  public String getSession() {
    return session;
  }

  /**
   * Get the flags attached to this entry. Clients can choose to use this however makes sense for their application.
   *
   * @return the flags attached to this entry
   */
  public long getFlags() {
    return flags;
  }

  /**
   * Get the internal index value that represents when the entry was created.
   *
   * @return the internal index value that represents when the entry was created.
   */
  public long getCreateIndex() {
    return createIndex;
  }

  /**
   * Get the last index that modified this key.
   *
   * @return the last index that modified this key.
   */
  public long getModifyIndex() {
    return modifyIndex;
  }

  /**
   * Get the number of times this key has successfully been acquired in a lock.
   *
   * @return the number of times this key has successfully been acquired in a lock.
   */
  public long getLockIndex() {
    return lockIndex;
  }
}

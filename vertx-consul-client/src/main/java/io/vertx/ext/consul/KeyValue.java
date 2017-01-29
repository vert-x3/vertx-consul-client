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
@DataObject(generateConverter = true)
public class KeyValue implements TxnResult {

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
    KeyValueConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    KeyValueConverter.toJson(this, jsonObject);
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
   * Set the key
   *
   * @param key the key
   * @return reference to this, for fluency
   */
  public KeyValue setKey(String key) {
    this.key = key;
    return this;
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
   * Set the value
   *
   * @param value the value
   * @return reference to this, for fluency
   */
  public KeyValue setValue(String value) {
    this.value = value;
    return this;
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
   * Set the session that owns the lock
   *
   * @param session the session that owns the lock
   * @return reference to this, for fluency
   */
  public KeyValue setSession(String session) {
    this.session = session;
    return this;
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
   * Set the flags attached to this entry. Clients can choose to use this however makes sense for their application.
   *
   * @param flags the flags attached to this entry. Clients can choose to use this however makes sense for their application.
   * @return reference to this, for fluency
   */
  public KeyValue setFlags(long flags) {
    this.flags = flags;
    return this;
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
   * Set the internal index value that represents when the entry was created.
   *
   * @param createIndex the internal index value that represents when the entry was created.
   * @return reference to this, for fluency
   */
  public KeyValue setCreateIndex(long createIndex) {
    this.createIndex = createIndex;
    return this;
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
   * Set the last index that modified this key.
   *
   * @param modifyIndex the last index that modified this key.
   * @return reference to this, for fluency
   */
  public KeyValue setModifyIndex(long modifyIndex) {
    this.modifyIndex = modifyIndex;
    return this;
  }

  /**
   * Get the number of times this key has successfully been acquired in a lock.
   *
   * @return the number of times this key has successfully been acquired in a lock.
   */
  public long getLockIndex() {
    return lockIndex;
  }

  /**
   * Set the number of times this key has successfully been acquired in a lock.
   *
   * @param lockIndex the number of times this key has successfully been acquired in a lock.
   * @return reference to this, for fluency
   */
  public KeyValue setLockIndex(long lockIndex) {
    this.lockIndex = lockIndex;
    return this;
  }
}

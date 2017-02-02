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
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;

/**
 * Holds operation to apply to the key/value store inside a transaction
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class TxnKVOperation implements TxnOperation {

  private TxnKVVerb type;
  private String key;
  private String value;
  private long flags;
  private long index;
  private String session;

  /**
   * Default constructor
   */
  public TxnKVOperation() {
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public TxnKVOperation(JsonObject json) {
    TxnKVOperationConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    TxnKVOperationConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get the type of operation to perform
   *
   * @return the type of operation to perform
   */
  public TxnKVVerb getType() {
    return type;
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
   * Get the flags attached to this entry. Clients can choose to use this however makes sense for their application.
   *
   * @return the flags attached to this entry
   */
  public long getFlags() {
    return flags;
  }

  /**
   * Get the index used for locking, unlocking, and check-and-set operations.
   *
   * @return the index
   * @see <a href="https://www.consul.io/docs/agent/http/kv.html#txn">/v1/txn</a> endpoint
   */
  public long getIndex() {
    return index;
  }

  /**
   * Get the session used for locking, unlocking, and check-and-set operations.
   *
   * @return the session
   * @see <a href="https://www.consul.io/docs/agent/http/kv.html#txn">/v1/txn</a> endpoint
   */
  public String getSession() {
    return session;
  }

  /**
   * Set the type of operation to perform
   *
   * @param type the type of operation to perform
   * @return reference to this, for fluency
   */
  public TxnKVOperation setType(TxnKVVerb type) {
    this.type = type;
    return this;
  }

  /**
   * Set the key
   *
   * @param key the key
   * @return reference to this, for fluency
   */
  public TxnKVOperation setKey(String key) {
    this.key = key;
    return this;
  }

  /**
   * Set the value
   *
   * @param value the value
   * @return reference to this, for fluency
   */
  public TxnKVOperation setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Set the flags attached to this entry. Clients can choose to use this however makes sense for their application.
   *
   * @param flags the flags attached to this entry. Clients can choose to use this however makes sense for their application.
   * @return reference to this, for fluency
   */
  public TxnKVOperation setFlags(long flags) {
    this.flags = flags;
    return this;
  }

  /**
   * Set the index used for locking, unlocking, and check-and-set operations.
   *
   * @param index the index
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/docs/agent/http/kv.html#txn">/v1/txn</a> endpoint
   */
  public TxnKVOperation setIndex(long index) {
    this.index = index;
    return this;
  }

  /**
   * Set the session used for locking, unlocking, and check-and-set operations.
   *
   * @param session the session
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/docs/agent/http/kv.html#txn">/v1/txn</a> endpoint
   */
  public TxnKVOperation setSession(String session) {
    this.session = session;
    return this;
  }

  @GenIgnore
  @Override
  public TxnOperationType getOperationType() {
    return TxnOperationType.KV;
  }
}

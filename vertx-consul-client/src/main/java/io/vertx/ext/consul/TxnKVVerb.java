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

/**
 * Holds type of KV operation in transaction
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/agent/http/kv.html#txn">/v1/txn</a> endpoint
 */
public enum TxnKVVerb {

  /**
   * Sets the Key to the given Value
   */
  SET("set"),

  /**
   * Sets the Key to the given Value with check-and-set semantics.
   * The Key will only be set if its current modify index matches the supplied Index
   */
  CAS("cas"),

  /**
   * Locks the Key with the given Session. The Key will only obtain the lock
   * if the Session is valid, and no other session has it locked
   */
  LOCK("lock"),

  /**
   * Unlocks the Key with the given Session. The Key will only release the lock
   * if the Session is valid and currently has it locked
   */
  UNLOCK("unlock"),

  /**
   * Gets the Key during the transaction. This fails the transaction if the Key doesn't exist.
   * The key may not be present in the results if ACLs do not permit it to be read
   */
  GET("get"),

  /**
   * Gets all keys with a prefix of Key during the transaction. This does not fail the transaction
   * if the Key doesn't exist. Not all keys may be present in the results if ACLs do not permit them to be read
   */
  GET_TREE("get-tree"),

  /**
   * Fails the transaction if Key does not have a modify index equal to Index
   */
  CHECK_INDEX("check-index"),

  /**
   * Fails the transaction if Key is not currently locked by Session
   */
  CHECK_SESSION("check-session"),

  /**
   * Deletes the Key
   */
  DELETE("delete"),

  /**
   * Deletes all keys with a prefix ofKey
   */
  DELETE_TREE("delete-tree"),

  /**
   * Deletes the Key with check-and-set semantics. The Key will only be deleted
   * if its current modify index matches the supplied Index
   */
  DELETE_CAS("delete-cas");

  public static TxnKVVerb ofVerb(String verb) {
    for (TxnKVVerb type : values()) {
      if (type.getVerb().equals(verb)) {
        return type;
      }
    }
    return null;
  }

  private final String verb;

  TxnKVVerb(String verb) {
    this.verb = verb;
  }

  public String getVerb() {
    return verb;
  }
}

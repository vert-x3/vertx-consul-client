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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static io.vertx.ext.consul.impl.Utils.decode64;
import static io.vertx.ext.consul.impl.Utils.encode64;

/**
 * Holds list of operations in transaction
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class TxnRequest {

  private List<TxnOperation> operations = new ArrayList<>();

  /**
   * Default constructor
   */
  public TxnRequest() {
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public TxnRequest(JsonObject json) {
    if (json.containsKey("operations")) {
      json.getJsonArray("operations").forEach(entry -> {
        JsonObject obj = (JsonObject) entry;
        if (obj.containsKey("KV")) {
          JsonObject txn = obj.getJsonObject("KV");
          operations.add(new TxnKVOperation()
            .setKey(txn.getString("Key"))
            .setValue(decode64(txn.getString("Value")))
            .setFlags(txn.getLong("Flags"))
            .setIndex(txn.getLong("Index"))
            .setSession(txn.getString("Session"))
            .setType(TxnKVVerb.ofVerb(txn.getString("Verb"))));
        }
      });
    }
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonArray arr = new JsonArray();
    operations.forEach(op -> {
      if (op instanceof TxnKVOperation) {
        TxnKVOperation kvOp = (TxnKVOperation) op;
        JsonObject obj = new JsonObject()
          .put("Verb", kvOp.getType().getVerb())
          .put("Key", kvOp.getKey())
          .put("Value", encode64(kvOp.getValue()))
          .put("Flags", kvOp.getFlags())
          .put("Index", kvOp.getIndex())
          .put("Session", kvOp.getSession());
        arr.add(new JsonObject().put("KV", obj));
      }
    });
    return new JsonObject().put("operations", arr);
  }

  /**
   * Returns list of transaction operations
   *
   * @return list of transaction operations
   */
  public List<TxnOperation> getOperations() {
    return operations;
  }

  /**
   * Returns the number of operations in this request
   *
   * @return the number of operations in this request
   */
  public int getOperationsSize() {
    return operations.size();
  }

  /**
   * Returns the operation at the specified position in this list
   *
   * @param index index of the operation to return
   * @return the operation at the specified position in this list
   */
  public TxnOperation getOperations(int index) {
    return operations.get(index);
  }

  /**
   * Adds operation to this request
   *
   * @param operation operation to add
   * @return reference to this, for fluency
   */
  public TxnRequest addOperation(TxnOperation operation) {
    operations.add(operation);
    return this;
  }
}

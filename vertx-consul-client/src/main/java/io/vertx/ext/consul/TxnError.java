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
 * Holds information describing which operations failed if the transaction was rolled back.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class TxnError {

  private int opIndex;
  private String what;

  /**
   * Default constructor
   */
  public TxnError() {
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public TxnError(JsonObject json) {
    TxnErrorConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    TxnErrorConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get the index of the failed operation in the transaction
   *
   * @return the index of the failed operation in the transaction
   */
  public int getOpIndex() {
    return opIndex;
  }

  /**
   * Get error message about why that operation failed.
   *
   * @return error message about why that operation failed.
   */
  public String getWhat() {
    return what;
  }

  /**
   * Set the index of the failed operation in the transaction
   *
   * @param opIndex the index of the failed operation in the transaction
   * @return reference to this, for fluency
   */
  public TxnError setOpIndex(int opIndex) {
    this.opIndex = opIndex;
    return this;
  }

  /**
   * Set error message about why that operation failed.
   *
   * @param what error message about why that operation failed.
   * @return reference to this, for fluency
   */
  public TxnError setWhat(String what) {
    this.what = what;
    return this;
  }
}

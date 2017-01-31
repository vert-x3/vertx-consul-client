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

/**
 * Holds results of transaction
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class TxnResponse {

  private final List<TxnResult> results = new ArrayList<>();
  private final List<TxnError> errors = new ArrayList<>();

  /**
   * Default constructor
   */
  public TxnResponse() {
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public TxnResponse(JsonObject json) {
    if (json.getValue("Results") instanceof JsonArray) {
      json.getJsonArray("Results").forEach(entry -> {
        JsonObject obj = (JsonObject) entry;
        if (obj.containsKey("KV")) {
          results.add(new KeyValue(obj.getJsonObject("KV")));
        }
      });
    }
    if (json.getValue("Errors") instanceof JsonArray) {
      json.getJsonArray("Errors").forEach(entry -> errors.add(new TxnError((JsonObject) entry)));
    }
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonArray jsonResults = new JsonArray();
    results.forEach(op -> {
      if (op instanceof KeyValue) {
        jsonResults.add(new JsonObject().put("KV", ((KeyValue) op).toJson()));
      }
    });
    JsonArray jsonErrors = new JsonArray();
    errors.forEach(err -> jsonErrors.add(err.toJson()));
    return new JsonObject().put("Results", jsonResults).put("Errors", jsonErrors);
  }

  /**
   * Returns list of transaction results
   *
   * @return list of transaction results
   */
  public List<TxnResult> getResults() {
    return results;
  }

  /**
   * Returns the number of results in this response
   *
   * @return the number of results in this response
   */
  public int getResultsSize() {
    return results.size();
  }

  /**
   * Returns the result at the specified position in this list
   *
   * @param index index of the result to return
   * @return the result at the specified position in this list
   */
  public TxnResult getResult(int index) {
    return results.get(index);
  }

  /**
   * Adds result to this response
   *
   * @param result the result
   * @return reference to this, for fluency
   */
  public TxnResponse addResult(TxnResult result) {
    results.add(result);
    return this;
  }

  /**
   * Returns list of transaction errors
   *
   * @return list of transaction errors
   */
  public List<TxnError> getErrors() {
    return errors;
  }

  /**
   * Returns the number of errors in this response
   *
   * @return the number of errors in this response
   */
  public int getErrorsSize() {
    return errors.size();
  }

  /**
   * Returns the errors at the specified position in this list
   *
   * @param index index of the error to return
   * @return the error at the specified position in this list
   */
  public TxnError getError(int index) {
    return errors.get(index);
  }

  /**
   * Adds error to this response
   *
   * @param error the error
   * @return reference to this, for fluency
   */
  public TxnResponse addError(TxnError error) {
    errors.add(error);
    return this;
  }
}

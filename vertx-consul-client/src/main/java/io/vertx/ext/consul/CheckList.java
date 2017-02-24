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

import java.util.List;

/**
 * Holds result of checks query
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class CheckList {

  private long index;
  private List<Check> list;

  /**
   * Default constructor
   */
  public CheckList() {}

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public CheckList(JsonObject json) {
    CheckListConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    CheckListConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get Consul index
   *
   * @return the consul index
   */
  public long getIndex() {
    return index;
  }

  /**
   * Set Consul index, a unique identifier representing the current state of the requested list of checks
   *
   * @param index the consul index
   * @return reference to this, for fluency
   */
  public CheckList setIndex(long index) {
    this.index = index;
    return this;
  }

  /**
   * Get list of checks
   *
   * @return the list of checks
   */
  public List<Check> getList() {
    return list;
  }

  /**
   * Set list of checks
   *
   * @param list the list of checks
   * @return reference to this, for fluency
   */
  public CheckList setList(List<Check> list) {
    this.list = list;
    return this;
  }
}

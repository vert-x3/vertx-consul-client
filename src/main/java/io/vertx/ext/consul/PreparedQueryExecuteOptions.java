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
 * Options used to execute prepared query
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class PreparedQueryExecuteOptions {

  private int limit;
  private String near;

  /**
   * Default constructor
   */
  public PreparedQueryExecuteOptions() {}

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public PreparedQueryExecuteOptions(JsonObject json) {
    PreparedQueryExecuteOptionsConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    PreparedQueryExecuteOptionsConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get node name for sorting the list in ascending order based on the estimated round trip time from that node.
   *
   * @return the node name
   */
  public String getNear() {
    return near;
  }

  /**
   * Set node name for sorting the list in ascending order based on the estimated round trip time from that node.
   * Passing {@code _agent} will use the agent's node for the sort. If this is not present,
   * the default behavior will shuffle the nodes randomly each time the query is executed.
   *
   * @param near the node name
   * @return reference to this, for fluency
   */
  public PreparedQueryExecuteOptions setNear(String near) {
    this.near = near;
    return this;
  }

  /**
   * Get the size of the list to the given number of nodes. This is applied after any sorting or shuffling.
   *
   * @return the size of the list to the given number of nodes
   */
  public int getLimit() {
    return limit;
  }

  /**
   * Set the size of the list to the given number of nodes. This is applied after any sorting or shuffling.
   *
   * @param limit the size of the list to the given number of nodes
   * @return reference to this, for fluency
   */
  public PreparedQueryExecuteOptions setLimit(int limit) {
    this.limit = limit;
    return this;
  }
}

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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds result of nodes query
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class NodeList {

  private static final String INDEX_KEY = "Index";
  private static final String LIST_KEY = "List";

  private long index;
  private List<Node> list;

  /**
   * Default constructor
   */
  public NodeList() {}

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public NodeList(JsonObject json) {
    index = json.getLong(INDEX_KEY, 0L);
    list = json.getJsonArray(LIST_KEY, new JsonArray()).stream()
      .map(obj -> new Node((JsonObject) obj)).collect(Collectors.toList());
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (index > 0) {
      jsonObject.put(INDEX_KEY, index);
    }
    if (list != null) {
      jsonObject.put(LIST_KEY, list.stream().map(Node::toJson).collect(Collectors.toList()));
    }
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
   * Set Consul index, a unique identifier representing the current state of the requested list of services
   *
   * @param index the consul index
   * @return reference to this, for fluency
   */
  public NodeList setIndex(long index) {
    this.index = index;
    return this;
  }

  /**
   * Get list of nodes
   *
   * @return the list of nodes
   */
  public List<Node> getList() {
    return list;
  }

  /**
   * Set list of nodes
   *
   * @param list the list of nodes
   * @return reference to this, for fluency
   */
  public NodeList setList(List<Node> list) {
    this.list = list;
    return this;
  }
}

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Holds result of nodes query
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class NodeList {

  private long index;
  private List<Node> list;

  /**
   * Default constructor
   */
  public NodeList() {}

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public NodeList(NodeList other) {
    this.index = other.index;
    this.list = other.list;
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public NodeList(JsonObject json) {
    NodeListConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    NodeListConverter.toJson(this, jsonObject);
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
   * Set Consul index, a unique identifier representing the current state of the requested list of nodes
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NodeList nodeList = (NodeList) o;

    if (index != nodeList.index) return false;
    return list != null ? sorted().equals(nodeList.sorted()) : nodeList.list == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (index ^ (index >>> 32));
    result = 31 * result + (list != null ? sorted().hashCode() : 0);
    return result;
  }

  private List<Node> sorted() {
    List<Node> sorted = null;
    if (list != null) {
      sorted = new ArrayList<>(list);
      sorted.sort(Comparator.comparing(Node::getName));
    }
    return sorted;
  }
}

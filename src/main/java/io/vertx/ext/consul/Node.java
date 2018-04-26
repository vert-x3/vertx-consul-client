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
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class Node {

  private String node;
  private String address;
  private String lanAddress;
  private String wanAddress;

  /**
   * Default constructor
   */
  public Node() {}

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public Node(Node other) {
    this.node = other.node;
    this.address = other.address;
    this.lanAddress = other.lanAddress;
    this.wanAddress = other.wanAddress;
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public Node(JsonObject json) {
    NodeConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    NodeConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get node name
   *
   * @return node name
   */
  public String getName() {
    return node;
  }

  /**
   * Get node address
   *
   * @return node address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Get node lan address
   *
   * @return node lan address
   */
  public String getLanAddress() {
    return lanAddress;
  }

  /**
   * Get node wan address
   *
   * @return node wan address
   */
  public String getWanAddress() {
    return wanAddress;
  }

  /**
   * Set node name
   *
   * @param node node name
   * @return reference to this, for fluency
   */
  public Node setName(String node) {
    this.node = node;
    return this;
  }

  /**
   * Set node address
   *
   * @param address node address
   * @return reference to this, for fluency
   */
  public Node setAddress(String address) {
    this.address = address;
    return this;
  }

  /**
   * Set node lan address
   *
   * @param lanAddress node lan address
   * @return reference to this, for fluency
   */
  public Node setLanAddress(String lanAddress) {
    this.lanAddress = lanAddress;
    return this;
  }

  /**
   * Set node wan address
   *
   * @param wanAddress node wan address
   * @return reference to this, for fluency
   */
  public Node setWanAddress(String wanAddress) {
    this.wanAddress = wanAddress;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Node node1 = (Node) o;

    if (node != null ? !node.equals(node1.node) : node1.node != null) return false;
    if (address != null ? !address.equals(node1.address) : node1.address != null) return false;
    if (lanAddress != null ? !lanAddress.equals(node1.lanAddress) : node1.lanAddress != null) return false;
    return wanAddress != null ? wanAddress.equals(node1.wanAddress) : node1.wanAddress == null;
  }

  @Override
  public int hashCode() {
    int result = node != null ? node.hashCode() : 0;
    result = 31 * result + (address != null ? address.hashCode() : 0);
    result = 31 * result + (lanAddress != null ? lanAddress.hashCode() : 0);
    result = 31 * result + (wanAddress != null ? wanAddress.hashCode() : 0);
    return result;
  }
}

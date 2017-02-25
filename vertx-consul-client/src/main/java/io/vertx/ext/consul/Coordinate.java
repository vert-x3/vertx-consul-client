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
 * Holds network coordinates of node
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/internals/coordinates.html">Network coordinates</a>
 */
@DataObject(generateConverter = true)
public class Coordinate {

  private String node;
  private float adj;
  private float err;
  private float height;
  private List<Float> vec;

  /**
   * Default constructor
   */
  public Coordinate() {}

  /**
   * Copy constructor
   *
   * @param coordinate the one to copy
   */
  public Coordinate(Coordinate coordinate) {
    this.node = coordinate.node;
    this.adj = coordinate.adj;
    this.err = coordinate.err;
    this.height = coordinate.height;
    this.vec = coordinate.vec;
  }

  /**
   * Constructor from JSON
   *
   * @param coordinate the JSON
   */
  public Coordinate(JsonObject coordinate) {
    CoordinateConverter.fromJson(coordinate, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    CoordinateConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get name of node
   *
   * @return name of node
   */
  public String getNode() {
    return node;
  }

  /**
   * Get adjustment
   *
   * @return adjustment
   */
  public float getAdj() {
    return adj;
  }

  /**
   * Get error
   *
   * @return error
   */
  public float getErr() {
    return err;
  }

  /**
   * Get height
   *
   * @return height
   */
  public float getHeight() {
    return height;
  }

  /**
   * Get vector
   *
   * @return vector
   */
  public List<Float> getVec() {
    return vec;
  }

  /**
   * Set name of node
   *
   * @param node name of node
   * @return reference to this, for fluency
   */
  public Coordinate setNode(String node) {
    this.node = node;
    return this;
  }

  /**
   * Set adjustment
   *
   * @param adj adjustment
   * @return reference to this, for fluency
   */
  public Coordinate setAdj(float adj) {
    this.adj = adj;
    return this;
  }

  /**
   * Set error
   *
   * @param err error
   * @return reference to this, for fluency
   */
  public Coordinate setErr(float err) {
    this.err = err;
    return this;
  }

  /**
   * Set height
   *
   * @param height height
   * @return reference to this, for fluency
   */
  public Coordinate setHeight(float height) {
    this.height = height;
    return this;
  }

  /**
   * Set vector
   *
   * @param vec vector
   * @return reference to this, for fluency
   */
  public Coordinate setVec(List<Float> vec) {
    this.vec = vec;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Coordinate that = (Coordinate) o;

    if (Float.compare(that.adj, adj) != 0) return false;
    if (Float.compare(that.err, err) != 0) return false;
    if (Float.compare(that.height, height) != 0) return false;
    if (node != null ? !node.equals(that.node) : that.node != null) return false;
    return vec != null ? vec.equals(that.vec) : that.vec == null;
  }

  @Override
  public int hashCode() {
    int result = node != null ? node.hashCode() : 0;
    result = 31 * result + (adj != +0.0f ? Float.floatToIntBits(adj) : 0);
    result = 31 * result + (err != +0.0f ? Float.floatToIntBits(err) : 0);
    result = 31 * result + (height != +0.0f ? Float.floatToIntBits(height) : 0);
    result = 31 * result + (vec != null ? vec.hashCode() : 0);
    return result;
  }
}

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
 * Holds result of network coordinates query
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class CoordinateList {

  private long index;
  private List<Coordinate> list;

  /**
   * Default constructor
   */
  public CoordinateList() {}

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public CoordinateList(CoordinateList other) {
    this.index = other.index;
    this.list = other.list;
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public CoordinateList(JsonObject json) {
    CoordinateListConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    CoordinateListConverter.toJson(this, jsonObject);
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
   * Get list of coordinates
   *
   * @return the list of coordinates
   */
  public List<Coordinate> getList() {
    return list;
  }

  /**
   * Set Consul index, a unique identifier representing the current state of the requested coordinates
   *
   * @param index the consul index
   * @return reference to this, for fluency
   */
  public CoordinateList setIndex(long index) {
    this.index = index;
    return this;
  }

  /**
   * Set list of coordinates
   *
   * @param list the list of coordinates
   * @return reference to this, for fluency
   */
  public CoordinateList setList(List<Coordinate> list) {
    this.list = list;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CoordinateList that = (CoordinateList) o;

    if (index != that.index) return false;
    return list != null ? sorted().equals(that.sorted()) : that.list == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (index ^ (index >>> 32));
    result = 31 * result + (list != null ? sorted().hashCode() : 0);
    return result;
  }

  private List<Coordinate> sorted() {
    List<Coordinate> sorted = null;
    if (list != null) {
      sorted = new ArrayList<>(list);
      sorted.sort(Comparator.comparing(Coordinate::getNode));
    }
    return sorted;
  }
}

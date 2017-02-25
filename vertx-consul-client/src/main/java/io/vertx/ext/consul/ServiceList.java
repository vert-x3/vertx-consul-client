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
 * Holds result of services query
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class ServiceList {

  private long index;
  private List<Service> list;

  /**
   * Default constructor
   */
  public ServiceList() {}

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public ServiceList(ServiceList other) {
    this.index = other.index;
    this.list = other.list;
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public ServiceList(JsonObject json) {
    ServiceListConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ServiceListConverter.toJson(this, jsonObject);
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
  public ServiceList setIndex(long index) {
    this.index = index;
    return this;
  }

  /**
   * Get list of services
   *
   * @return the list of services
   */
  public List<Service> getList() {
    return list;
  }

  /**
   * Set list of services
   *
   * @param list the list of services
   * @return reference to this, for fluency
   */
  public ServiceList setList(List<Service> list) {
    this.list = list;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ServiceList that = (ServiceList) o;

    if (index != that.index) return false;
    return list != null ? sorted().equals(that.sorted()) : that.list == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (index ^ (index >>> 32));
    result = 31 * result + (list != null ? sorted().hashCode() : 0);
    return result;
  }

  private List<Service> sorted() {
    List<Service> sorted = null;
    if (list != null) {
      sorted = new ArrayList<>(list);
      sorted.sort(Comparator.comparing(Service::getId));
    }
    return sorted;
  }
}

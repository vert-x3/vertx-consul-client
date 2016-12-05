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
 * Holds result of services query
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class ServiceList {

  private static final String INDEX_KEY = "Index";
  private static final String LIST_KEY = "List";

  private long index;
  private List<Service> list;

  /**
   * Default constructor
   */
  public ServiceList() {}

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public ServiceList(JsonObject json) {
    index = json.getLong(INDEX_KEY, 0L);
    list = json.getJsonArray(LIST_KEY, new JsonArray()).stream()
      .map(obj -> new Service((JsonObject) obj)).collect(Collectors.toList());
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
      jsonObject.put(LIST_KEY, list.stream().map(Service::toJson).collect(Collectors.toList()));
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
}

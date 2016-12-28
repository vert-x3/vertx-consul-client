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
 * Holds list of services, nodes and related checks
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class ServiceEntryList {

  private long index;
  private List<ServiceEntry> list;

  /**
   * Default constructor
   */
  public ServiceEntryList() {}

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public ServiceEntryList(JsonObject json) {
    ServiceEntryListConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ServiceEntryListConverter.toJson(this, jsonObject);
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
  public ServiceEntryList setIndex(long index) {
    this.index = index;
    return this;
  }

  /**
   * Get list of services
   *
   * @return list of services
   */
  public List<ServiceEntry> getList() {
    return list;
  }

  /**
   * Set list of services
   *
   * @param list list of services
   * @return reference to this, for fluency
   */
  public ServiceEntryList setList(List<ServiceEntry> list) {
    this.list = list;
    return this;
  }
}

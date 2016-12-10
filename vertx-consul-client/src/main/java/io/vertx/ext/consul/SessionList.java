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
 * Holds result of sessions query
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class SessionList {

  private long index;
  private List<Session> list;

  /**
   * Default constructor
   */
  public SessionList() {}

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public SessionList(JsonObject json) {
    SessionListConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    SessionListConverter.toJson(this, jsonObject);
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
   * Set Consul index, a unique identifier representing the current state of the requested list of sessions
   *
   * @param index the consul index
   * @return reference to this, for fluency
   */
  public SessionList setIndex(long index) {
    this.index = index;
    return this;
  }

  /**
   * Get list of sessions
   *
   * @return the list of sessions
   */
  public List<Session> getList() {
    return list;
  }

  /**
   * Set list of sessions
   *
   * @param list the list of sessions
   * @return reference to this, for fluency
   */
  public SessionList setList(List<Session> list) {
    this.list = list;
    return this;
  }
}

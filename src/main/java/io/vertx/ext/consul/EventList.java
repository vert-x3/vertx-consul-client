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
 * Holds result of events query

 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class EventList {

  private long index;
  private List<Event> list;

  /**
   * Default constructor
   */
  public EventList() {}

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public EventList(EventList other) {
    this.index = other.index;
    this.list = other.list;
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public EventList(JsonObject json) {
    EventListConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    EventListConverter.toJson(this, jsonObject);
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
   * Set Consul index, a unique identifier representing the current state of the requested events
   *
   * @param index the consul index
   * @return reference to this, for fluency
   */
  public EventList setIndex(long index) {
    this.index = index;
    return this;
  }

  /**
   * Get list of events
   *
   * @return the list of events
   */
  public List<Event> getList() {
    return list;
  }

  /**
   * Set list of events
   *
   * @param list the list of events
   * @return reference to this, for fluency
   */
  public EventList setList(List<Event> list) {
    this.list = list;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EventList eventList = (EventList) o;

    if (index != eventList.index) return false;
    return list != null ? sorted().equals(eventList.sorted()) : eventList.list == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (index ^ (index >>> 32));
    result = 31 * result + (list != null ? sorted().hashCode() : 0);
    return result;
  }

  private List<Event> sorted() {
    List<Event> sorted = null;
    if (list != null) {
      sorted = new ArrayList<>(list);
      sorted.sort(Comparator.comparing(Event::getId));
    }
    return sorted;
  }
}

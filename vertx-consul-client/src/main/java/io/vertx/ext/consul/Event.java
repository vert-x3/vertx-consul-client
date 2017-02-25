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
 * Holds properties of Consul event
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class Event {

  private String id;
  private String name;
  private String payload;
  private String node;
  private String service;
  private String tag;
  private int version;
  private int lTime;

  /**
   * Default constructor
   */
  public Event() {}

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public Event(Event other) {
    this.id = other.id;
    this.name = other.name;
    this.payload = other.payload;
    this.node = other.node;
    this.service = other.service;
    this.tag = other.tag;
    this.version = other.version;
    this.lTime = other.lTime;
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public Event(JsonObject json) {
    EventConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    EventConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get ID of event
   *
   * @return ID of event
   */
  public String getId() {
    return id;
  }

  /**
   * Set ID of event
   *
   * @param id ID of event
   * @return reference to this, for fluency
   */
  public Event setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get name of event
   *
   * @return event name
   */
  public String getName() {
    return name;
  }

  /**
   * Set name of event
   *
   * @param name name of event
   * @return reference to this, for fluency
   */
  public Event setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get payload of event
   *
   * @return payload
   */
  public String getPayload() {
    return payload;
  }

  /**
   * Set payload of event
   *
   * @param payload payload of event
   * @return reference to this, for fluency
   */
  public Event setPayload(String payload) {
    this.payload = payload;
    return this;
  }

  /**
   * Get regular expression to filter by node name
   *
   * @return regular expression to filter by node name
   */
  public String getNode() {
    return node;
  }

  /**
   * Set regular expression to filter by node name
   *
   * @param node regular expression to filter by node name
   * @return reference to this, for fluency
   */
  public Event setNode(String node) {
    this.node = node;
    return this;
  }

  /**
   * Get regular expression to filter by service
   *
   * @return regular expression to filter by service
   */
  public String getService() {
    return service;
  }

  /**
   * Set regular expression to filter by service
   *
   * @param service regular expression to filter by service
   * @return reference to this, for fluency
   */
  public Event setService(String service) {
    this.service = service;
    return this;
  }

  /**
   * Get regular expression to filter by tag
   *
   * @return regular expression to filter by tag
   */
  public String getTag() {
    return tag;
  }

  /**
   * Set regular expression to filter by tag
   *
   * @param tag regular expression to filter by tag
   * @return reference to this, for fluency
   */
  public Event setTag(String tag) {
    this.tag = tag;
    return this;
  }

  /**
   * Get version
   *
   * @return version
   */
  public int getVersion() {
    return version;
  }

  /**
   * Set version
   *
   * @param version version
   * @return reference to this, for fluency
   */
  public Event setVersion(int version) {
    this.version = version;
    return this;
  }

  /**
   * Get the Lamport clock time
   *
   * @return the Lamport clock time
   */
  public int getLTime() {
    return lTime;
  }

  /**
   * Set the Lamport clock time
   *
   * @param lTime the Lamport clock time
   * @return reference to this, for fluency
   */
  public Event setLTime(int lTime) {
    this.lTime = lTime;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Event event = (Event) o;

    if (version != event.version) return false;
    if (lTime != event.lTime) return false;
    if (id != null ? !id.equals(event.id) : event.id != null) return false;
    if (name != null ? !name.equals(event.name) : event.name != null) return false;
    if (payload != null ? !payload.equals(event.payload) : event.payload != null) return false;
    if (node != null ? !node.equals(event.node) : event.node != null) return false;
    if (service != null ? !service.equals(event.service) : event.service != null) return false;
    return tag != null ? tag.equals(event.tag) : event.tag == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (payload != null ? payload.hashCode() : 0);
    result = 31 * result + (node != null ? node.hashCode() : 0);
    result = 31 * result + (service != null ? service.hashCode() : 0);
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    result = 31 * result + version;
    result = 31 * result + lTime;
    return result;
  }
}

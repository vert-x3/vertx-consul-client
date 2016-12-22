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

}

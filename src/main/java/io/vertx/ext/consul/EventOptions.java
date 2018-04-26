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
 * Options used to trigger a new user event.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class EventOptions {

  private String node;
  private String service;
  private String tag;
  private String payload;

  /**
   * Default constructor
   */
  public EventOptions() {
  }

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public EventOptions(EventOptions options) {
    this.node = options.node;
    this.service = options.service;
    this.tag = options.tag;
    this.payload = options.payload;
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public EventOptions(JsonObject options) {
    EventOptionsConverter.fromJson(options, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    EventOptionsConverter.toJson(this, jsonObject);
    return jsonObject;
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
  public EventOptions setNode(String node) {
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
  public EventOptions setService(String service) {
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
  public EventOptions setTag(String tag) {
    this.tag = tag;
    return this;
  }

  /**
   * Get payload of event
   *
   * @return payload of event
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
  public EventOptions setPayload(String payload) {
    this.payload = payload;
    return this;
  }
}

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
 * Holds options for events list request
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class EventListOptions {

  private String name;
  private BlockingQueryOptions options;

  /**
   * Default constructor
   */
  public EventListOptions() {}

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public EventListOptions(JsonObject json) {
    EventListOptionsConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    EventListOptionsConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get event name for filtering on events
   *
   * @return name of the event
   */
  public String getName() {
    return name;
  }

  /**
   * Get options for blocking query
   *
   * @return the blocking options
   */
  public BlockingQueryOptions getBlockingOptions() {
    return options;
  }

  /**
   * Set event name for filtering on events
   *
   * @param name name of the event
   * @return reference to this, for fluency
   */
  public EventListOptions setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Set options for blocking query
   *
   * @param options the blocking options
   * @return reference to this, for fluency
   */
  public EventListOptions setBlockingOptions(BlockingQueryOptions options) {
    this.options = options;
    return this;
  }
}

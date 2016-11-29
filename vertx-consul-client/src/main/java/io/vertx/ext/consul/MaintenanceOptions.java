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
 * Options used to placing a given service into "maintenance mode".
 * During maintenance mode, the service will be marked as unavailable
 * and will not be present in DNS or API queries. Maintenance mode is persistent
 * and will be automatically restored on agent restart.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class MaintenanceOptions {

  private static final String ID_KEY = "ID";
  private static final String ENABLE_KEY = "Enable";
  private static final String REASON_KEY = "Reason";

  private String id;
  private boolean enable;
  private String reason;

  /**
   * Default constructor
   */
  public MaintenanceOptions() {
  }

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public MaintenanceOptions(MaintenanceOptions options) {
    id = options.id;
    enable = options.enable;
    reason = options.reason;
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public MaintenanceOptions(JsonObject options) {
    id = options.getString(ID_KEY);
    enable = options.getBoolean(ENABLE_KEY);
    reason = options.getString(REASON_KEY);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (id != null) {
      jsonObject.put(ID_KEY, id);
    }
    jsonObject.put(ENABLE_KEY, enable);
    if (reason != null) {
      jsonObject.put(REASON_KEY, reason);
    }
    return jsonObject;
  }

  /**
   * Set the ID of service. This field is required.
   *
   * @param id the ID of service
   * @return reference to this, for fluency
   */
  public MaintenanceOptions setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get the ID of service
   *
   * @return the ID of service
   */
  public String getId() {
    return id;
  }

  /**
   * Set maintenance mode to enabled: {@code true} to enter maintenance mode or {@code false} to resume normal operation.
   * This flag is required.
   *
   * @param enable maintenance mode
   * @return reference to this, for fluency
   */
  public MaintenanceOptions setEnable(boolean enable) {
    this.enable = enable;
    return this;
  }

  /**
   * Get maintenance mode
   *
   * @return the boolean if maintenance mode enabled
   */
  public boolean isEnable() {
    return enable;
  }

  /**
   * Set the reason message. If provided, its value should be a text string explaining the reason for placing
   * the service into maintenance mode. This is simply to aid human operators.
   *
   * @param reason the reason message
   * @return reference to this, for fluency
   */
  public MaintenanceOptions setReason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get the reason message
   *
   * @return the reason message
   */
  public String getReason() {
    return reason;
  }
}

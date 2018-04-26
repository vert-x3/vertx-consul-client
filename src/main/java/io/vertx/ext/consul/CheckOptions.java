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
 * Options used to register checks in Consul.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class CheckOptions {

  private String id;
  private String name;
  private String script;
  private String http;
  private String ttl;
  private String tcp;
  private String interval;
  private String notes;
  private String serviceId;
  private String deregisterAfter;
  private CheckStatus status;

  /**
   * Default constructor
   */
  public CheckOptions() {
  }

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public CheckOptions(CheckOptions options) {
    this.id = options.id;
    this.name = options.name;
    this.script = options.script;
    this.http = options.http;
    this.ttl = options.ttl;
    this.tcp = options.tcp;
    this.interval = options.interval;
    this.notes = options.notes;
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public CheckOptions(JsonObject options) {
    CheckOptionsConverter.fromJson(options, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    CheckOptionsConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get the service ID to associate the registered check with an existing service provided by the agent.
   *
   * @return the service ID
   */
  public String getServiceId() {
    return serviceId;
  }

  /**
   * Set the service ID to associate the registered check with an existing service provided by the agent.
   *
   * @param serviceId the service ID
   * @return reference to this, for fluency
   */
  public CheckOptions setServiceId(String serviceId) {
    this.serviceId = serviceId;
    return this;
  }

  /**
   * Get the check status to specify the initial state of the health check.
   *
   * @return the check status
   */
  public CheckStatus getStatus() {
    return status;
  }

  /**
   * Set the check status to specify the initial state of the health check.
   *
   * @param status the check status
   * @return reference to this, for fluency
   */
  public CheckOptions setStatus(CheckStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get path to checking script
   *
   * @return path to script
   */
  public String getScript() {
    return script;
  }

  /**
   * Set path to checking script. Also you should set checking interval
   *
   * @param script path to script
   * @return reference to this, for fluency
   */
  public CheckOptions setScript(String script) {
    this.script = script;
    return this;
  }

  /**
   * Get HTTP address
   *
   * @return HTTP address
   */
  public String getHttp() {
    return http;
  }

  /**
   * Set HTTP address to check. Also you should set checking interval
   *
   * @param http HTTP address
   * @return reference to this, for fluency
   */
  public CheckOptions setHttp(String http) {
    this.http = http;
    return this;
  }

  /**
   * Get Time to Live of check
   *
   * @return Time to Live of check
   */
  public String getTtl() {
    return ttl;
  }

  /**
   * Set Time to Live of check.
   *
   * @param ttl Time to Live of check
   * @return reference to this, for fluency
   */
  public CheckOptions setTtl(String ttl) {
    this.ttl = ttl;
    return this;
  }

  /**
   * Get TCP address. Also you should set checking interval
   *
   * @return TCP address
   */
  public String getTcp() {
    return tcp;
  }

  /**
   * Set TCP address to check. Also you should set checking interval
   *
   * @param tcp TCP address
   * @return reference to this, for fluency
   */
  public CheckOptions setTcp(String tcp) {
    this.tcp = tcp;
    return this;
  }

  /**
   * Get checking interval
   *
   * @return interval
   */
  public String getInterval() {
    return interval;
  }

  /**
   * Set checking interval
   *
   * @param interval checking interval in Go's time format which is sequence of decimal numbers,
   *                 each with optional fraction and a unit suffix, such as "300ms", "-1.5h" or "2h45m".
   *                 Valid time units are "ns", "us" (or "µs"), "ms", "s", "m", "h"
   * @return reference to this, for fluency
   */
  public CheckOptions setInterval(String interval) {
    this.interval = interval;
    return this;
  }

  /**
   * Get check ID
   *
   * @return check ID
   */
  public String getId() {
    return id;
  }

  /**
   * Set check ID
   *
   * @param id check ID
   * @return reference to this, for fluency
   */
  public CheckOptions setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get check name
   *
   * @return check name
   */
  public String getName() {
    return name;
  }

  /**
   * Set check name. This is mandatory field
   *
   * @param name check name
   * @return reference to this, for fluency
   */
  public CheckOptions setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get check notes
   *
   * @return check notes
   */
  public String getNotes() {
    return notes;
  }

  /**
   * Set check notes
   *
   * @param notes check notes
   * @return reference to this, for fluency
   */
  public CheckOptions setNotes(String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get deregister timeout
   *
   * @return timeout
   */
  public String getDeregisterAfter() {
    return deregisterAfter;
  }

  /**
   * Set deregister timeout. This is optional field, which is a timeout in the same time format as Interval and TTL.
   * If a check is associated with a service and has the critical state for more than this configured value,
   * then its associated service (and all of its associated checks) will automatically be deregistered.
   * The minimum timeout is 1 minute, and the process that reaps critical services runs every 30 seconds,
   * so it may take slightly longer than the configured timeout to trigger the deregistration.
   * This should generally be configured with a timeout that's much, much longer than any expected recoverable outage
   * for the given service.
   *
   * @param deregisterAfter timeout
   * @return reference to this, for fluency
   */
  public CheckOptions setDeregisterAfter(String deregisterAfter) {
    this.deregisterAfter = deregisterAfter;
    return this;
  }
}

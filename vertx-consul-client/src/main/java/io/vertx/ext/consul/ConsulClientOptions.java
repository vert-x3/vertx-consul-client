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
 * Options used to create Consul client.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class ConsulClientOptions {

  private String host;
  private int port;
  private String aclToken;
  private String dc;
  private long timeoutMs;

  /**
   * Default constructor
   */
  public ConsulClientOptions() {
  }

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public ConsulClientOptions(ConsulClientOptions options) {
    this.host = options.host;
    this.port = options.port;
    this.aclToken = options.aclToken;
    this.dc = options.dc;
    this.timeoutMs = options.timeoutMs;
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public ConsulClientOptions(JsonObject options) {
    ConsulClientOptionsConverter.fromJson(options, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ConsulClientOptionsConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get Consul host.
   *
   * @return consul host
   */
  public String getHost() {
    return host;
  }

  /**
   * Get Consul HTTP API port.
   *
   * @return consul port
   */
  public int getPort() {
    return port;
  }

  /**
   * Get the ACL token.
   *
   * @return the ACL token.
   */
  public String getAclToken() {
    return aclToken;
  }

  /**
   * Get the datacenter name
   *
   * @return the datacenter name
   */
  public String getDc() {
    return dc;
  }

  /**
   * Get timeout in milliseconds
   *
   * @return timeout in milliseconds
   */
  public long getTimeoutMs() {
    return timeoutMs;
  }

  /**
   * Set Consul host. Defaults to `localhost`
   *
   * @param host consul host
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setHost(String host) {
    this.host = host;
    return this;
  }

  /**
   * Set Consul HTTP API port. Defaults to `8500`
   *
   * @param port Consul HTTP API port
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setPort(int port) {
    this.port = port;
    return this;
  }

  /**
   * Set the ACL token. When provided, the client will use this token when making requests to the Consul
   * by providing the "?token" query parameter. When not provided, the empty token, which maps to the 'anonymous'
   * ACL policy, is used.
   *
   * @param aclToken the ACL token
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setAclToken(String aclToken) {
    this.aclToken = aclToken;
    return this;
  }

  /**
   * Set the datacenter name. When provided, the client will use it when making requests to the Consul
   * by providing the "?dc" query parameter. When not provided, the datacenter of the consul agent is queried.
   *
   * @param dc the datacenter name
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setDc(String dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Sets the amount of time (in milliseconds) after which if the request does not return any data
   * within the timeout period an failure will be passed to the handler and the request will be closed.
   *
   * @param timeoutMs timeout in milliseconds
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setTimeoutMs(long timeoutMs) {
    this.timeoutMs = timeoutMs;
    return this;
  }
}

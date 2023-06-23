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
 * Options used to requesting list of checks
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class CheckQueryOptions {

  private String near;
  private BlockingQueryOptions options;
  /**
   * Specifies the datacenter to query
   */
  private String dc;
  /**
   * Specifies the expression used to filter the queries results prior to returning the data
   *
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/health#filtering">Filtering</a>
   */
  private String filter;
  /**
   * Specifies the namespace to list checks
   */
  private String ns;

  /**
   * Default constructor
   */
  public CheckQueryOptions() {
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public CheckQueryOptions(JsonObject json) {
    CheckQueryOptionsConverter.fromJson(json, this);
  }

  /**
   * Returns the datacenter that ultimately provided the list of nodes
   *
   * @return the datacenter that ultimately provided the list of nodes
   */
  public String getDc() {
    return dc;
  }

  /**
   * Set an optional datacenter. This will default to the datacenter of the agent being queried
   *
   * @param dc datacenter
   */
  public CheckQueryOptions setDc(String dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Returns the expression used to filter the queries results prior to returning the data
   *
   * @return string of selectors and filter operations
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/health#filtering">Filtering</a>
   */
  public String getFilter() {
    return filter;
  }

  /**
   * Set the expression to filter the queries results prior to returning the data
   *
   * @param expression string of selectors and filter operations
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/health#filtering">Supported selectors and operations</a>
   */
  public CheckQueryOptions setFilter(String expression) {
    this.filter = expression;
    return this;
  }

  /**
   * Returns the namespace to list checks
   *
   * @return namespace
   */

  public String getNs() {
    return ns;
  }

  /**
   * Sets the optional namespace to list checks. By default, the namespace will be inherited from the request's ACL token
   * or will default to the default namespace.
   *
   * @param namespace
   */
  public CheckQueryOptions setNs(String namespace) {
    this.ns = namespace;
    return this;
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    CheckQueryOptionsConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get node name for sorting the list in ascending order based on the estimated round trip time from that node.
   *
   * @return the node name
   */
  public String getNear() {
    return near;
  }

  /**
   * Set node name for sorting the list in ascending order based on the estimated round trip time from that node.
   *
   * @param near the node name
   * @return reference to this, for fluency
   */
  public CheckQueryOptions setNear(String near) {
    this.near = near;
    return this;
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
   * Set options for blocking query
   *
   * @param options the blocking options
   * @return reference to this, for fluency
   */
  public CheckQueryOptions setBlockingOptions(BlockingQueryOptions options) {
    this.options = options;
    return this;
  }
}

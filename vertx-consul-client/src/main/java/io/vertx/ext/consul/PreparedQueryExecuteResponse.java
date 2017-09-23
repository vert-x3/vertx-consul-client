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
 * The results of executing prepared query
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class PreparedQueryExecuteResponse {

  private String service;
  private String dc;
  private String dnsTtl;
  private int failovers;
  private List<ServiceEntry> nodes;

  /**
   * Default constructor
   */
  public PreparedQueryExecuteResponse() {
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public PreparedQueryExecuteResponse(JsonObject json) {
    PreparedQueryExecuteResponseConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    PreparedQueryExecuteResponseConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get the service name that the query was selecting.
   * This is useful for context in case an empty list of nodes is returned.
   *
   * @return the service name that the query was selecting.
   */
  public String getService() {
    return service;
  }

  /**
   * Set the service name that the query was selecting.
   * This is useful for context in case an empty list of nodes is returned.
   *
   * @param service the service name that the query was selecting.
   * @return reference to this, for fluency
   */
  public PreparedQueryExecuteResponse setService(String service) {
    this.service = service;
    return this;
  }

  /**
   * Get the datacenter that ultimately provided the list of nodes
   *
   * @return the datacenter that ultimately provided the list of nodes
   */
  public String getDc() {
    return dc;
  }

  /**
   * Set the datacenter that ultimately provided the list of nodes
   *
   * @param dc the datacenter that ultimately provided the list of nodes
   * @return reference to this, for fluency
   */
  public PreparedQueryExecuteResponse setDc(String dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Get the TTL duration when query results are served over DNS. If this is specified, it will take precedence over any Consul agent-specific configuration.
   *
   * @return the TTL duration
   */
  public String getDnsTtl() {
    return dnsTtl;
  }

  /**
   * Set the TTL duration when query results are served over DNS. If this is specified, it will take precedence over any Consul agent-specific configuration.
   *
   * @param dnsTtl the TTL duration
   * @return reference to this, for fluency
   */
  public PreparedQueryExecuteResponse setDnsTtl(String dnsTtl) {
    this.dnsTtl = dnsTtl;
    return this;
  }

  /**
   * Get the number of remote datacenters that were queried while executing the query.
   *
   * @return the number of remote datacenters that were queried while executing the query.
   */
  public int getFailovers() {
    return failovers;
  }

  /**
   * Set the number of remote datacenters that were queried while executing the query.
   *
   * @param failovers the number of remote datacenters that were queried while executing the query.
   * @return reference to this, for fluency
   */
  public PreparedQueryExecuteResponse setFailovers(int failovers) {
    this.failovers = failovers;
    return this;
  }

  /**
   * Get the list of healthy nodes providing the given service, as specified by the constraints of the prepared query.
   *
   * @return the list of healthy nodes providing the given service, as specified by the constraints of the prepared query.
   */
  public List<ServiceEntry> getNodes() {
    return nodes;
  }

  /**
   * Set the list of healthy nodes providing the given service, as specified by the constraints of the prepared query.
   *
   * @param nodes the list of healthy nodes providing the given service, as specified by the constraints of the prepared query.
   * @return reference to this, for fluency
   */
  public PreparedQueryExecuteResponse setNodes(List<ServiceEntry> nodes) {
    this.nodes = nodes;
    return this;
  }
}

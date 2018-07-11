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
import io.vertx.ext.consul.impl.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds properties of service, node and related checks
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class ServiceEntry {

  private Node node;
  private Service service;
  private List<Check> checks;

  /**
   * Default constructor
   */
  public ServiceEntry() {}

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public ServiceEntry(ServiceEntry other) {
    this.node = other.node;
    this.service = other.service;
    this.checks = other.checks;
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public ServiceEntry(JsonObject json) {
    ServiceEntryConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ServiceEntryConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * AggregatedStatus returns the "best" status for the list of health checks.
   * Because a given entry may have many service and node-level health checks
   * attached, this function determines the best representative of the status as
   * as single string using the following heuristic:
   *
   * critical > warning > passing
   *
   * @return an aggregated status
   */
  public CheckStatus aggregatedStatus() {
    return Utils.aggregateCheckStatus(checks.stream().map(Check::getStatus).collect(Collectors.toList()));
  }

  /**
   * Get node
   *
   * @return node
   */
  public Node getNode() {
    return node;
  }

  /**
   * Get service
   *
   * @return service
   */
  public Service getService() {
    return service;
  }

  /**
   * Get list of checks
   *
   * @return list of checks
   */
  public List<Check> getChecks() {
    return checks;
  }

  /**
   * Set node
   *
   * @param node node
   * @return reference to this, for fluency
   */
  public ServiceEntry setNode(Node node) {
    this.node = node;
    return this;
  }

  /**
   * Set service
   *
   * @param service service
   * @return reference to this, for fluency
   */
  public ServiceEntry setService(Service service) {
    this.service = service;
    return this;
  }

  /**
   * Set list of checks
   *
   * @param checks list of checks
   * @return reference to this, for fluency
   */
  public ServiceEntry setChecks(List<Check> checks) {
    this.checks = checks;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ServiceEntry entry = (ServiceEntry) o;

    if (node != null ? !node.equals(entry.node) : entry.node != null) return false;
    if (service != null ? !service.equals(entry.service) : entry.service != null) return false;
    return checks != null ? sorted().equals(entry.sorted()) : entry.checks == null;
  }

  @Override
  public int hashCode() {
    int result = node != null ? node.hashCode() : 0;
    result = 31 * result + (service != null ? service.hashCode() : 0);
    result = 31 * result + (checks != null ? sorted().hashCode() : 0);
    return result;
  }

  private List<Check> sorted() {
    List<Check> sorted = null;
    if (checks != null) {
      sorted = new ArrayList<>(checks);
      sorted.sort(Comparator.comparing(Check::getId));
    }
    return sorted;
  }
}

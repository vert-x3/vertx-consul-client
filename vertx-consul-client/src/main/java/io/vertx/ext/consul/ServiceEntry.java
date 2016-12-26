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
}
